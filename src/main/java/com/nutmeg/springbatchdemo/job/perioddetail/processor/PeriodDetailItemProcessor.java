package com.nutmeg.springbatchdemo.job.perioddetail.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.nutmeg.springbatchdemo.database.dao.TradedModelDao;
import com.nutmeg.springbatchdemo.database.dao.WebFundDao;
import com.nutmeg.springbatchdemo.job.perioddetail.validation.FixedInvestment;
import com.nutmeg.springbatchdemo.model.PeriodDetail;
import com.nutmeg.springbatchdemo.model.WebFund;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import javafx.util.Pair;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Named
@StepScope
public class PeriodDetailItemProcessor implements ItemProcessor<PeriodDetail, WebFund> {


    private static final String NEW_RISK_JSON_NODE = "risk_level_new";
    private static final String OLD_RISK_JSON_NODE = "risk_level_old";
    private static final String TIMESTAMP_JSON_NODE = "timeStamp";
    private static final String MODEL_PREFIX = "M";
    private static final String NEW_INV_STYLE = "investment_style_new";
    private static final String OLD_INV_STYLE = "investment_style_old";
    private static final String FIXED_INV_STYLE = "fixed";

    private final Integer tradeTimestamp;

    private final ObjectMapper objectMapper;

    private final WebFundDao webFundDao;

    private final TradedModelDao tradedModelDao;

    public PeriodDetailItemProcessor(
        @Value("#{jobParameters['tradeTimestamp']}") final Integer tradeTimestamp,
        final ObjectMapper objectMapper,
        final WebFundDao webFundDao,
        final TradedModelDao tradedModelDao
        ) {
        this.tradeTimestamp = tradeTimestamp;
        this.objectMapper = objectMapper;
        this.webFundDao = webFundDao;
        this.tradedModelDao = tradedModelDao;
    }

    @Override
    public WebFund process(PeriodDetail periodDetail) throws Exception {
        WebFund webFund = getWebFund(periodDetail.getFundUuid());
        JsonNode changeJson = objectMapper.readTree(Optional.ofNullable(webFund.getChangeJson()).orElse("{}"));
        Map<Integer, Pair<String, String>> sortedChanges = sortAndGetChanges(changeJson);
        Optional<String> modelOptional = hasModelBefore(sortedChanges);
        if (!modelOptional.isPresent()) {
            modelOptional = hasModelAfter(sortedChanges);
        }
        if (!modelOptional.isPresent()) {
            validateCurrentModel(webFund.getCurrentModel(), periodDetail.getFundUuid());
        }
        String model = modelOptional.orElse(webFund.getCurrentModel());
        log.info("'{}', '{}'", periodDetail.getFundUuid(), model);
        webFund.setCurrentModel(model);
        return webFund;
    }

    @VisibleForTesting
    Optional<String> hasModelBefore(Map<Integer, Pair<String, String>> sortedChanges) {
        Optional<Integer> timeStamp = sortedChanges.keySet().stream()
            .filter(t -> t < tradeTimestamp)
            .findFirst();
        return timeStamp.isPresent() ? Optional.of(sortedChanges.get(timeStamp.get()).getKey()) : Optional.empty();
    }

    @VisibleForTesting
    Optional<String> hasModelAfter(Map<Integer, Pair<String, String>> sortedChanges) {
        Optional<Integer> timeStamp = sortedChanges.keySet().stream()
            .sorted()
            .filter(t -> t >= tradeTimestamp)
            .findFirst();
        return timeStamp.isPresent() ? Optional.of(sortedChanges.get(timeStamp.get()).getValue()) : Optional.empty();
    }

    private WebFund getWebFund(String uuid) throws Exception {
        return webFundDao.find(uuid).orElseThrow(
            () -> new Exception(String.format("Fund: %s not found.", uuid))
        );
    }

    private Map<Integer, Pair<String, String>> sortAndGetChanges(JsonNode jsonNode) {
        Map<Integer,  Pair<String, String>> sortedChanges = new TreeMap<>(Collections.reverseOrder());
        if (jsonNode.isArray()) {
            for (final JsonNode objNode : jsonNode) {
                if (objNode.has(NEW_RISK_JSON_NODE)) {
                    sortedChanges.put(
                        Integer.parseInt(objNode.get(TIMESTAMP_JSON_NODE).asText()),
                        new Pair<>(
                            getNewModelByInvestmentType(objNode),
                            getOldModelByInvestmentType(objNode))
                    );
                }
            }
        }
        return sortedChanges;
    }

    private void validateCurrentModel(String model, String fundUuid) throws Exception {
        Integer numberModelsTraded = tradedModelDao.countDistinctModelByFund(fundUuid);
        if (numberModelsTraded > 1) {
            throw new Exception(String.format("Fund %s traded with %d models. Current %s", fundUuid, numberModelsTraded, model));
        }
    }

    private String getNewModelByInvestmentType(JsonNode jsonNode) {
        if (jsonNode.has(NEW_INV_STYLE) && FIXED_INV_STYLE.equals(jsonNode.get(NEW_INV_STYLE).asText())) {
            return buildFixedModel(jsonNode, NEW_RISK_JSON_NODE);
        }
        return buildAllocatedModel(jsonNode, NEW_RISK_JSON_NODE);
    }

    private String getOldModelByInvestmentType(JsonNode jsonNode) {
        if (jsonNode.has(OLD_INV_STYLE) && FIXED_INV_STYLE.equals(jsonNode.get(OLD_INV_STYLE).asText())) {
            return buildFixedModel(jsonNode, OLD_RISK_JSON_NODE);
        }
        return buildAllocatedModel(jsonNode, OLD_RISK_JSON_NODE);
    }

    private String buildFixedModel(JsonNode jsonNode, String risk) {
        return MODEL_PREFIX + FixedInvestment.values()[Integer.parseInt(jsonNode.get(risk).asText())-1];
    }

    private String buildAllocatedModel(JsonNode jsonNode, String risk) {
        return MODEL_PREFIX + jsonNode.get(risk).asText();
    }
}
