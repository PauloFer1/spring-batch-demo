package com.nutmeg.springbatchdemo.mapper;

import com.nutmeg.springbatchdemo.model.TradedModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.inject.Named;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

@Named
public class TradedModelRowMapper implements RowMapper<TradedModel> {
    @Override
    public TradedModel map(ResultSet resultSet, StatementContext ctx) throws SQLException {
        return TradedModel.builder()
            .fundUuid(resultSet.getString("FUNDUUID"))
            .modelName(resultSet.getString("MODELNAME"))
            .createdOn(resultSet.getString("CREATEDON"))
            .build();
    }
}
