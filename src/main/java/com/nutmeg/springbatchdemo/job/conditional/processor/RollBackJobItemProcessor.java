package com.nutmeg.springbatchdemo.job.conditional.processor;

import com.nutmeg.springbatchdemo.model.Demo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import javax.inject.Named;

@Slf4j
@Named
public class RollBackJobItemProcessor implements ItemProcessor<Demo, Demo> {

    @Override
    public Demo process(Demo demo) throws Exception {
        log.info("Rollback Demo {}", demo.toString());
        demo.setIsValid(true);
        return demo;
    }
}
