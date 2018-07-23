package com.nutmeg.springbatchdemo.job.conditional.processor;

import com.nutmeg.springbatchdemo.model.Demo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import javax.inject.Named;
import java.math.BigDecimal;

@Slf4j
@Named
public class ConditionalJobItemProcessor implements ItemProcessor<Demo, Demo> {

    @Override
    public Demo process(Demo demo) throws Exception {
        log.info("Invalidating Demo {}", demo.toString());

        if (demo.getPrice().compareTo(new BigDecimal(399)) > 0) {
            throw new RuntimeException("Price was higher 399");
        }
        demo.setIsValid(false);
        return demo;
    }
}
