package com.nutmeg.springbatchdemo.database.dao;

import com.nutmeg.springbatchdemo.mapper.WebFundRowMapper;
import com.nutmeg.springbatchdemo.model.WebFund;
import java.util.Optional;
import javax.inject.Named;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

@Named
@RequiredArgsConstructor
public class JdbiWebFundDao implements WebFundDao {

    private static String SELECT_FROM_UUID = "select * from T_WEB_FUND where UUID = :uuid";

    private final Jdbi jdbi;

    @Override
    public Optional<WebFund> find(String uuid) {
        return jdbi.withHandle(h -> h.createQuery(SELECT_FROM_UUID)
            .bind("uuid", uuid)
            .map(new WebFundRowMapper())
            .findFirst()
        );
    }
}
