package com.nutmeg.springbatchdemo.database.dao;

import com.nutmeg.springbatchdemo.model.AccountPosting;
import java.util.Optional;

public interface AccountPostingDao {

    Optional<AccountPosting> find(final String uuid, final String date);
}
