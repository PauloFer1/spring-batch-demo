package com.nutmeg.springbatchdemo.service.twrr;

import com.nutmeg.springbatchdemo.database.dao.AccountPostingDao;
import com.nutmeg.springbatchdemo.exception.AccountPostingNotFoundException;
import com.nutmeg.springbatchdemo.model.AccountPosting;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Named;

@Named
@RequiredArgsConstructor
public class TwrrServiceJdbi implements TwrrService {

    private final Jdbi jdbi;

    @Override
    public AccountPosting get(String uuid, String date) throws AccountPostingNotFoundException {
        return jdbi.withExtension(AccountPostingDao.class,
                dao -> dao.find(uuid, date).orElseThrow(() -> new AccountPostingNotFoundException(uuid)));
    }
}
