package com.almasb.jm.dbms;

import java.util.Optional;

import com.almasb.jm.common.Account;

public interface DBMS {

    public boolean addEntry(Account account);

    public Optional<Account> getEntry(String email);

    public boolean updateEntry(Account account);

    public void close();

    //public void deleteEntry(String email);
}
