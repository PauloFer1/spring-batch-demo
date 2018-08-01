package com.nutmeg.springbatchdemo.job.conditional.processor;

import com.nutmeg.springbatchdemo.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import javax.inject.Named;
import java.math.BigDecimal;

@Slf4j
@Named
public class ConditionalJobItemProcessor implements ItemProcessor<Price, Price> {

    @Override
    public Price process(Price price) throws Exception {
        log.info("Invalidating Price {}", price.toString());

        if (price.getPrice().compareTo(new BigDecimal(19000)) > 0) {
            throw new RuntimeException("Price was higher 19000.00");
        }
        price.setIsValid(false);
        return price;
    }
}
