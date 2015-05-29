package com.almasb.jm.dbms;

import io.orchestrate.client.Client;
import io.orchestrate.client.KvMetadata;
import io.orchestrate.client.KvObject;
import io.orchestrate.client.OrchestrateClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import com.almasb.jm.common.Account;

// TODO: bg thread + ui thread design
public class OrchestrateDBMS implements DBMS {

    private Client client;

    public OrchestrateDBMS() throws Exception {
        client = OrchestrateClient.builder(readAPIKey())
                .host("https://api.aws-eu-west-1.orchestrate.io")
                .build();
    }

    @Override
    public boolean addEntry(Account account) {
        KvMetadata userMeta = client.kv("accounts", account.getEmail())
                      .put(account)   // sends the HTTP PUT request
                      .get();      // blocks and returns the response

        // print the 'ref' for the stored data
        System.out.println(userMeta.getRef());
        return true;
    }

    @Override
    public Optional<Account> getEntry(String email) {
        KvObject<Account> userKv = client.kv("accounts", email)
                      .get(Account.class)  // send the HTTP GET request
                      .get();           // block and return the response

        if (userKv != null) {
            return Optional.of(userKv.getValue());
        }

        return Optional.empty();
    }

    @Override
    public boolean updateEntry(Account account) {
        return addEntry(account);
    }

    @Override
    public void close() {
        try {
            client.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String readAPIKey() throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("orchestrate_api.key")))) {
            return reader.readLine();
        }
    }
}
