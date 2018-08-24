package com.nutmeg.springbatchdemo.database.dao;

import com.nutmeg.springbatchdemo.model.TradedModel;
import java.util.List;
import java.util.Optional;

public interface TradedModelDao {
    Optional<List<TradedModel>> findGroupedByModel(final String fundUuid);

    Integer countDistinctModelByFund(final String fundUuid);
}
