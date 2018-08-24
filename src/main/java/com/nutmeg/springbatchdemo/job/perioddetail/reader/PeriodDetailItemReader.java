package com.nutmeg.springbatchdemo.job.perioddetail.reader;

import com.nutmeg.springbatchdemo.mapper.PeriodDetailRowMapper;
import com.nutmeg.springbatchdemo.model.PeriodDetail;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcCursorItemReader;

@Named
public class PeriodDetailItemReader extends JdbcCursorItemReader<PeriodDetail> {

    private static final String GET_NO_MODEL_PERIOD_DETAIL = "select\n" +
        "\tDISTINCT\n" +
        "\tp.FUND_UUID_OWN\n" +
        "from\n" +
        "\tT_NUT_PERIODDETAIL as p\n" +
        "\tinner join T_NUT_ACCOUNTPOSTINGX as a on p.FUND_UUID_OWN = a.FUND_UUID_OWN\n" +
        "where\n" +
        "\tp.LASTMODEL = 'NO MODEL' and\n" +
        "\tp.perioddate = '20170126' and\n" +
        "\tp.periodtype = 'D' and\n" +
        "\tp.recordtype = 'FUND' and\n" +
        "\ta.DATE = '20170126' AND\n" +
        "\ta.SIGNEDTYPE = 'STK' AND\n" +
        "\tp.FUND_UUID_OWN not in (\n" +
        "\t\t'0d6b4a15-df7d-4884-a0b2-0dd9d84410e6',\n" +
        "\t\t'252a2a1b-24e8-4c94-97cf-834316f844b3',\n" +
        "\t\t'2774f07b-d06c-487a-9b8e-0cc4d616a55f',\n" +
        "\t\t'31a94479-308d-49ef-a066-4e897a0a1978',\n" +
        "\t\t'60166e89-2b4f-4dc2-b3d0-9a60a9a85cb1',\n" +
        "\t\t'e4fdaae3-4de9-4d8a-9682-612b47fc6925',\n" +
        "\t\t'3c44745e-7671-4ff2-996c-e553ec101e3a',\n" +
        "\t\t'8d14c5dd-dfce-4105-9006-df955e809e50',\n" +
        "\t\t'd6d6a34e-98cc-4d62-b769-232c24d49352',\n" +
        "\t\t'e5518f56-3f04-4002-a894-5e8e859cace2'\n" +
        "\t);";

    private PeriodDetail periodDetail;

    @Inject
    public PeriodDetailItemReader(@Named("nutmegDataSource") final DataSource dataSource,
                                  final PeriodDetailRowMapper periodDetailRowMapper) {
        setSql(GET_NO_MODEL_PERIOD_DETAIL);
        setDataSource(dataSource);
        setRowMapper(periodDetailRowMapper);
    }

    @Override
    public PeriodDetail read() throws Exception, UnexpectedInputException, ParseException {
        synchronized (this) {
            periodDetail = super.read();
        }
        return periodDetail;
    }
}
