package com.nutmeg.springbatchdemo.service.twrr;

import com.nutmeg.springbatchdemo.exception.AccountPostingNotFoundException;
import com.nutmeg.springbatchdemo.model.AccountPosting;

public interface TwrrService {

    AccountPosting get(String uuid, String date) throws AccountPostingNotFoundException;
}
