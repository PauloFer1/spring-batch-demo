package com.nutmeg.springbatchdemo.job.twrr.processor;

import com.nutmeg.springbatchdemo.model.AccountPosting;
import com.nutmeg.springbatchdemo.model.AccountPostingCsv;
import com.nutmeg.springbatchdemo.service.twrr.TwrrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.math.BigDecimal;

@Slf4j
@Named
@StepScope
public class TwrrItemProcessor implements ItemProcessor<AccountPostingCsv, AccountPostingCsv> {

    private static final String MANUAL_TYPE = "zzz";

    private final BigDecimal fromErrorPercentage;
    private final BigDecimal toErrorPercentage;
    private final TwrrService twrrService;

    public TwrrItemProcessor(
            @Value("#{jobParameters['fromErrorPercentage']}") final BigDecimal fromErrorPercentage,
            @Value("#{jobParameters['toErrorPercentage']}") final BigDecimal toErrorPercentage,
            final TwrrService twrrService;
    ) {
        this.fromErrorPercentage = fromErrorPercentage;
        this.toErrorPercentage = toErrorPercentage;
        this.twrrService = twrrService;
    }

    @Override
    public AccountPostingCsv process(AccountPostingCsv accountPostingCsv) throws Exception {
        log.info("Processing: {}", accountPostingCsv.toString());

        BigDecimal itemErrorPercentage = new BigDecimal(accountPostingCsv.getAbsoluteError());
        if (fromErrorPercentage.compareTo(itemErrorPercentage) < 0 && toErrorPercentage.compareTo(itemErrorPercentage) > 0) {
            log.info("{} is between {} and {}", itemErrorPercentage, fromErrorPercentage, toErrorPercentage);
            AccountPosting accountPosting = twrrService.get(accountPostingCsv.getFundUuidOwn(), accountPostingCsv.getDate());
            if (MANUAL_TYPE.equals(accountPosting.getType())
                    && accountPosting.getValue().equals(new BigDecimal(accountPostingCsv.getDifference()))) {
                return accountPostingCsv;
            }
        }
        return null;
    }
}
