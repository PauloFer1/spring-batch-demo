package com.nutmeg.springbatchdemo.mapper;

import com.nutmeg.springbatchdemo.model.AccountPosting;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.inject.Named;
import java.sql.ResultSet;
import java.sql.SQLException;

@Named
public class AccountPostingRowMapper implements RowMapper<AccountPosting> {
    @Override
    public AccountPosting map(ResultSet rs, StatementContext ctx) throws SQLException {
        return AccountPosting.builder()
                .uuid(rs.getString("UUID"))
                .value(rs.getBigDecimal("VALUE"))
                .type(rs.getString("TYPE"))
                .build();
    }
}
