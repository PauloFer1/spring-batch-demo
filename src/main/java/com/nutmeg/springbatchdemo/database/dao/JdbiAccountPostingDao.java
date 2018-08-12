package com.nutmeg.springbatchdemo.database.dao;

import com.nutmeg.springbatchdemo.mapper.AccountPostingRowMapper;
import com.nutmeg.springbatchdemo.model.AccountPosting;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Named;
import java.util.Optional;

@Named
@RequiredArgsConstructor
public class JdbiAccountPostingDao implements AccountPostingDao {

    private static String SELECT_FROM_UUID_AND_DATE = "SELECT * FROM T_NUT_ACCOUNTPOSTINGX " +
            "WHERE FUND_UUID_OWN = :uuid AND DATE=:date";

    private final Jdbi jdbi;

    @Override
    public Optional<AccountPosting> find(final String uuid, final String date) {
        return jdbi.withHandle(h -> h.createQuery(SELECT_FROM_UUID_AND_DATE)
                .bind("uuid", uuid)
                .bind("date", date)
                .map(new AccountPostingRowMapper())
                .findFirst()
        );
    }
}
