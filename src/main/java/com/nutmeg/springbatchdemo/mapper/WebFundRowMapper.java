package com.nutmeg.springbatchdemo.mapper;

import com.nutmeg.springbatchdemo.model.WebFund;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.inject.Named;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

@Named
public class WebFundRowMapper implements RowMapper<WebFund> {
    @Override
    public WebFund map(ResultSet resultSet, StatementContext ctx) throws SQLException {
        return WebFund.builder()
            .uuid(resultSet.getString("UUID"))
            .currentModel(resultSet.getString("CURRENT_MODEL"))
            .changeJson(resultSet.getString("CHANGE_JSON"))
            .build();
    }
}
