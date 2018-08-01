package com.nutmeg.springbatchdemo.job.simple.processor;

import com.nutmeg.springbatchdemo.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Named
@StepScope
public class SimpleJobItemProcessor implements ItemProcessor<Price, Price> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String updateDate;
    private final int fee;

    public SimpleJobItemProcessor(
            @Value("#{jobParameters['updateDate']}") final String updateDate,
            @Value("#{jobParameters['fee']}") final int fee) {
        this.updateDate = updateDate;
        this.fee = fee;
    }

    @Override
    public Price process(Price price) throws Exception {
        log.info("Adding Fees to {}", price.toString());

        // Simulate costly processing
        Thread.sleep(10);
        BigDecimal addFee = new BigDecimal(fee);
        price.setPrice(price.getPrice().add(addFee));
        price.setUpdatedAt(LocalDateTime.parse(updateDate, FORMATTER));
        return price;
    }
}
