package com.nutmeg.springbatchdemo.job.conditional.processor;

import com.nutmeg.springbatchdemo.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import javax.inject.Named;
import java.math.BigDecimal;

@Slf4j
@Named
public class MonthlyFeesJobItemProcessor implements ItemProcessor<Price, Price> {

    private static final BigDecimal MONTHLY_FEE = new BigDecimal("2.10");
    private static final String FEE_CURRENCY = "£";

    @Override
    public Price process(Price price) throws Exception {
        log.info("Adding {}{} Fee to {}", FEE_CURRENCY, MONTHLY_FEE, price.toString());
        price.setPrice(price.getPrice().add(MONTHLY_FEE));
        return price;
    }
}
