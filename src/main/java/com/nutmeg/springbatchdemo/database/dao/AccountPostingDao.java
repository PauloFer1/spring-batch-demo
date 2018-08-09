package com.nutmeg.springbatchdemo.database.dao;

import com.nutmeg.springbatchdemo.model.AccountPosting;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.Optional;

public interface AccountPostingDao extends SqlObject {

    String SELECT_FROM_UUID_AND_DATE = "SELECT * FROM T_NUT_ACCOUNTPOSTINGX" +
            "WHERE FUNDUUIDOWN = :uuid AND DATE=:date";

    @SqlQuery(SELECT_FROM_UUID_AND_DATE)
    @RegisterBeanMapper(AccountPosting.class)
    Optional<AccountPosting> find(@Bind("uuid") final String uuid, @Bind("date") String date);
}
