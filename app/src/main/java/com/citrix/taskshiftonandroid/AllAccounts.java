package com.citrix.taskshiftonandroid;

import java.util.HashMap;
import java.util.Map;

public class AllAccounts {
    private static Map<String, Account> accountMap = new HashMap<String, Account>();

    static void init() {
        Account TLAccount = new Account("carlostian927@berkeley.edu",
                "DwNBtNVKteYVQd7MjNHF0250", "5f03322ad6803200212f2dc0");
        accountMap.put("TL", TLAccount);
        Account LHRAccount = new Account("xeal3k@gmail.com",
                "dK9YeYe38KuOfEDacc0wCC34", "5f033116b545e200154e76f4");
        accountMap.put("LHR", LHRAccount);
    }

    static Account getAccount(String id) {
        return accountMap.get(id);
    }
}
