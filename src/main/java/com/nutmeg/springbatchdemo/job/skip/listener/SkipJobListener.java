package com.nutmeg.springbatchdemo.job.skip.listener;

import com.nutmeg.springbatchdemo.model.Price;
import com.nutmeg.springbatchdemo.model.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;

import javax.inject.Named;
import java.util.List;

@Slf4j
@Named
public class SkipJobListener implements SkipListener<Stock, List<Price>> {
    @Override
    public void onSkipInRead(Throwable throwable) {
        log.error("Skipped record during reading. Cause: {}", throwable.getMessage());
    }

    @Override
    public void onSkipInWrite(List<Price> prices, Throwable throwable) {
        log.error("Skipped item {} during writing . Cause: {}", prices.toString(), throwable.getMessage());
    }

    @Override
    public void onSkipInProcess(Stock stock, Throwable throwable) {
        log.error("Skipped item {} during processing. Cause: {}", stock.toString(), throwable.getMessage());
    }
}
