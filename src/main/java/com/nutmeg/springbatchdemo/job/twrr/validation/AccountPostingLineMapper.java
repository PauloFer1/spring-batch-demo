package com.nutmeg.springbatchdemo.job.twrr.validation;

import com.nutmeg.springbatchdemo.model.AccountPostingCsv;
import java.util.Arrays;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

@Named
public class AccountPostingLineMapper extends DefaultLineMapper<AccountPostingCsv> implements InitializingBean {

    private final int[] includedFields;

    @Inject
    public AccountPostingLineMapper(
            final @Value("${accountPosting.includedFields:2,3,5,6,7,8,9,14,15,16,17,18}") int[] includedFields
    ) {
        this.includedFields = Arrays.copyOf(includedFields, includedFields.length);
    }

    private void setLineTokenizer() {
        super.setLineTokenizer(new DelimitedLineTokenizer() {{
            setNames(
                    Arrays.stream(AccountPostingHeader.values())
                            .map(AccountPostingHeader::getValue)
                            .toArray(String[]::new)
            );
            // Only use needed fields
            setIncludedFields(includedFields);
        }});
    }

    private void setFieldSetMapper() {
        BeanWrapperFieldSetMapper<AccountPostingCsv> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper();
        beanWrapperFieldSetMapper.setTargetType(AccountPostingCsv.class);
        super.setFieldSetMapper(beanWrapperFieldSetMapper);
    }

    @Override
    public AccountPostingCsv mapLine(String line, int lineNumber) throws Exception {
        return super.mapLine(line, lineNumber);
    }

    @Override
    public void afterPropertiesSet() {
        setLineTokenizer();
        setFieldSetMapper();
        super.afterPropertiesSet();
    }
}