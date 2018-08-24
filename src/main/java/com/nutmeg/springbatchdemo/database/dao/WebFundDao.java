package com.nutmeg.springbatchdemo.database.dao;

import com.nutmeg.springbatchdemo.model.WebFund;
import java.util.Optional;

public interface WebFundDao {
    Optional<WebFund> find(final String uuid);
}
