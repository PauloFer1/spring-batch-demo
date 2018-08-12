package com.nutmeg.springbatchdemo.service.twrr;

import com.nutmeg.springbatchdemo.database.dao.AccountPostingDao;
import com.nutmeg.springbatchdemo.exception.AccountPostingNotFoundException;
import com.nutmeg.springbatchdemo.model.AccountPosting;
import lombok.RequiredArgsConstructor;

import javax.inject.Named;

@Named
@RequiredArgsConstructor
public class TwrrServiceJdbi implements TwrrService {

    private final AccountPostingDao accountPostingDao;

    @Override
    public AccountPosting get(final String uuid, final String date) throws AccountPostingNotFoundException {
        return accountPostingDao.find(uuid, date).orElseThrow(() -> new AccountPostingNotFoundException(uuid));
    }
}
