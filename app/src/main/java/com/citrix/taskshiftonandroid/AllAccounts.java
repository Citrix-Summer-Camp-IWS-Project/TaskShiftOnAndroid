package com.citrix.taskshiftonandroid;

import java.util.HashMap;
import java.util.Map;

public class AllAccounts {
    private static Map<String, Account> accountMap = new HashMap<String, Account>();

    static void init() {
        Account TLAccount = new Account("carlostian927@berkeley.edu",
                "DwNBtNVKteYVQd7MjNHF0250", "5f03322ad6803200212f2dc0",
                "https://auth.atlassian.com/authorize?audience=api.atlassian.com&" +
                "client_id=gA7g3MYgFxnsHfQauyGIMIFK5AtWPwC2&" +
                "scope=read%3Ajira-user%20read%3Ajira-work%20write%3Ajira-work%20offline_access&" +
                "redirect_uri=https%3A%2F%2Fbaidu.com&state=${YOUR_USER_BOUND_VALUE}&" +
                "response_type=code&prompt=consent",
                "gA7g3MYgFxnsHfQauyGIMIFK5AtWPwC2",
                "aj5P5sCvsNVLyfZvj0tsp5fA_SyVOliPWNvEvTwLtEMTVxjpERgHSm00l3F_SnVP");
        accountMap.put("TL", TLAccount);
        Account LHRAccount = new Account("xeal3k@gmail.com",
                "dK9YeYe38KuOfEDacc0wCC34", "5f033116b545e200154e76f4",
                "https://auth.atlassian.com/authorize?audience=api.atlassian.com&" +
                "client_id=wVYliO37FC1rRAcjs3ro2Kbl0OP4Opyk&" +
                "scope=read%3Ajira-user%20read%3Ajira-work%20write%3Ajira-work%20offline_access&" +
                "redirect_uri=https%3A%2F%2Fbaidu.com&state=${YOUR_USER_BOUND_VALUE}&" +
                "response_type=code&prompt=consent",
                "wVYliO37FC1rRAcjs3ro2Kbl0OP4Opyk",
                "Yv-tiqGXzi8vrqJ0PqE0oR52-7q53OpMWOEmB3-BR1kZpbN2rn_11iRIV9yYOwva");
        accountMap.put("LHR", LHRAccount);
    }

    static Account getAccount(String id) {
        return accountMap.get(id);
    }
}
