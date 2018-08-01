package com.nutmeg.springbatchdemo.job.skip.processor;

import com.nutmeg.springbatchdemo.model.Price;
import com.nutmeg.springbatchdemo.model.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Named
@StepScope
public class SkipJobItemProcessor implements ItemProcessor<Stock, List<Price>> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String updateDate;

    public SkipJobItemProcessor(@Value("#{jobParameters['updateDate']}") final String updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public List<Price> process(Stock stock) throws Exception {
        log.info("Adding History to {}", stock.toString());
        return IntStream.rangeClosed(0, 50)
                .mapToObj(x -> buildRandomPrice(stock.getUuid()))
                .collect(Collectors.toList());
    }

    private Price buildRandomPrice(String stockUuid) {
        BigDecimal randomPrice = new BigDecimal(1.00 + new Random().nextDouble() * (999.00));
        return Price.builder()
                .uuid(UUID.randomUUID().toString())
                .isValid(true)
                .price(randomPrice)
                .updatedAt(LocalDateTime.parse(updateDate, FORMATTER))
                .stockUuid(stockUuid)
                .build();
    }
}
