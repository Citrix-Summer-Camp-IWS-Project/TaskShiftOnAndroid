package com.citrix.taskshiftonandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private Account account;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button TL = (Button) findViewById(R.id.TL);
        Button LHR = (Button) findViewById(R.id.LHR);
        account = (Account) getApplication();
        TL.setOnClickListener(v -> {
            account.setTLUsername();
            account.setTLToken();
            account.setTLAccountID();
            Intent intent = new Intent(this,Launch.class);
            startActivity(intent);
        });
        LHR.setOnClickListener(v -> {
            account.setLHRUsername();
            account.setLHRToken();
            account.setLHRAccountID();
            Intent intent = new Intent(this,Launch.class);
            startActivity(intent);
        });
    }
















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
