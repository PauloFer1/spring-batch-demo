package com.nutmeg.springbatchdemo.database.dao;

import com.nutmeg.springbatchdemo.model.TradedModel;
import java.util.List;
import java.util.Optional;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import org.jdbi.v3.core.Jdbi;

@Named
@AllArgsConstructor
public class JdbiTradedModelDao implements TradedModelDao {

    private static final String SELECT_FROM_FUND_UUID_GROUP_BY_MODEL = "select\n" +
        "  *\n" +
        "from\n" +
        "  T_NUT_TRADEDMODEL\n" +
        "where\n" +
        "  FUNDUUID = :fundUuid\n" +
        "group by MODELNAME;";

    private static final String COUNT_FROM_FUND_UUID_GROUP_BY_MODEL = "select\n" +
        "  count(DISTINCT MODELNAME)\n" +
        "from\n" +
        "  T_NUT_TRADEDMODEL\n" +
        "where\n" +
        "  FUNDUUID = :fundUuid";

    private final Jdbi jdbi;

    @Override
    public Optional<List<TradedModel>> findGroupedByModel(String fundUuid) {
        return Optional.empty();
    }

    @Override
    public Integer countDistinctModelByFund(String fundUuid) {
        return jdbi.withHandle(h -> h.createQuery(COUNT_FROM_FUND_UUID_GROUP_BY_MODEL)
            .bind("fundUuid", fundUuid)
            .mapTo(Integer.class)
            .findFirst().orElse(0)
        );
    }
}
