package com.nutmeg.springbatchdemo.job.conditional.processor;

import com.nutmeg.springbatchdemo.model.Demo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import javax.inject.Named;
import java.math.BigDecimal;

@Slf4j
@Named
public class MonthlyFeesJobItemProcessor implements ItemProcessor<Demo, Demo> {

    private static final BigDecimal MONTHLY_FEE = new BigDecimal("2.10");
    private static final String FEE_CURRENCY = "£";

    @Override
    public Demo process(Demo demo) throws Exception {
        log.info("Adding {}{} Fee to {}", FEE_CURRENCY, MONTHLY_FEE, demo.toString());
        demo.setPrice(demo.getPrice().add(MONTHLY_FEE));
        return demo;
    }
}
