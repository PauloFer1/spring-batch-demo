package com.nutmeg.springbatchdemo.job.perioddetail.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutmeg.springbatchdemo.database.dao.TradedModelDao;
import com.nutmeg.springbatchdemo.database.dao.WebFundDao;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PeriodDetailItemProcessorTest {

    private static final Integer TRADE_TIMESTAMP = 1485426600;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private WebFundDao webFundDao;

    @Mock
    private TradedModelDao tradedModelDao;

    private PeriodDetailItemProcessor periodDetailItemProcessor;

    @Before
    public void setUp() {
        periodDetailItemProcessor = new PeriodDetailItemProcessor(
            TRADE_TIMESTAMP,
            objectMapper,
            webFundDao,
            tradedModelDao
        );
    }

    @Test
    public void hasModelBeforeReturnsFirstFind() {
        Map<Integer, Pair<String, String>> sortedChanges = new TreeMap<>(Collections.reverseOrder());
        sortedChanges.put(1485426500, new Pair<String,String>("M1", "M1"));
        sortedChanges.put(1485426501, new Pair<String,String>("M2", "M2"));
        sortedChanges.put(1485426509, new Pair<String,String>("M3", "M3"));
        sortedChanges.put(1485426401, new Pair<String,String>("M4", "M4"));
        sortedChanges.put(1485426601, new Pair<String,String>("M5", "M5"));
        sortedChanges.put(1485426602, new Pair<String,String>("M6", "M6"));
        sortedChanges.put(1485426402, new Pair<String,String>("M7", "M7"));
        sortedChanges.put(1485426504, new Pair<String,String>("M8", "M8"));

        Optional<String> result = periodDetailItemProcessor.hasModelBefore(sortedChanges);
        assertTrue(result.isPresent());
        assertEquals("M3", result.get());
    }

    @Test
    public void hasModelAfterReturnsFirstFind() {
        Map<Integer, Pair<String, String>> sortedChanges = new TreeMap<>(Collections.reverseOrder());
        sortedChanges.put(1485426610, new Pair<String,String>("M1", "M1"));
        sortedChanges.put(1485426609, new Pair<String,String>("M2", "M2"));
        sortedChanges.put(1485426601, new Pair<String,String>("M3", "M10"));
        sortedChanges.put(1485426605, new Pair<String,String>("M4", "M4"));
        sortedChanges.put(1485426603, new Pair<String,String>("M5", "M5"));
        sortedChanges.put(1485426606, new Pair<String,String>("M6", "M6"));

        Optional<String> result = periodDetailItemProcessor.hasModelAfter(sortedChanges);
        assertTrue(result.isPresent());
        assertEquals("M10", result.get());
    }
}