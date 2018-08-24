package com.nutmeg.springbatchdemo.mapper;

import com.nutmeg.springbatchdemo.model.PeriodDetail;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.inject.Named;
import org.springframework.jdbc.core.RowMapper;

@Named
public class PeriodDetailRowMapper implements RowMapper<PeriodDetail> {
    @Override
    public PeriodDetail mapRow(ResultSet resultSet, int i) throws SQLException {
        return PeriodDetail.builder()
            .fundUuid(resultSet.getString("FUND_UUID_OWN"))
            .build();
    }
}
