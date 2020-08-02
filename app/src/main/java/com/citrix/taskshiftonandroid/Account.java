package com.citrix.taskshiftonandroid;

import android.app.Application;

public class Account extends Application {
    public String username;
    public String token;
    public String AccountID;


    public String getUsername() {
        return username;
    }

    public void setTLUsername() {
        this.username = "carlostian927@berkeley.edu";
    }
    public void setLHRUsername() {
        this.username = "xeal3k@gmail.com";
    }

    public String getToken() {
        return token;
    }

    public void setTLToken() {
        this.token = "DwNBtNVKteYVQd7MjNHF0250";
    }
    public void setLHRToken() {
        this.token = "dK9YeYe38KuOfEDacc0wCC34";
    }

    public String getAccountID() {
        return AccountID;
    }

    public void setTLAccountID() {
        AccountID = "5f03322ad6803200212f2dc0";
    }
    public void setLHRAccountID() {
        AccountID = "5f033116b545e200154e76f4";
    }
}
