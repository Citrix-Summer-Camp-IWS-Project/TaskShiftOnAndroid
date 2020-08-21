package com.citrix.taskshiftonandroid;

import java.io.Serializable;

public class Account implements Serializable {
    private String username;
    private String token;
    private String accountID;

    Account(String username, String token, String accountID) {
        this.username = username;
        this.token = token;
        this.accountID = accountID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }
}
