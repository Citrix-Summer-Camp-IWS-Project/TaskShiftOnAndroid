package com.citrix.taskshiftonandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private Account TLAccount;
    private Account LHRAccount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final Button TL = (Button) findViewById(R.id.TL);
        Button LHR = (Button) findViewById(R.id.LHR);
        TL.setTag("TL");
        LHR.setTag("LHR");
        AllAccounts.init();
        TL.setOnClickListener(v -> {
            buttonHelper(v);
        });
        LHR.setOnClickListener(v -> {
            buttonHelper(v);
        });
    }

    void buttonHelper(View view) {
        Intent intent = new Intent(this, Web.class);
        intent.putExtra("Account", AllAccounts.getAccount(((Button) view).getTag().toString()));
        System.out.println("this is " + AllAccounts.getAccount(((Button) view).getTag().toString()));
        startActivity(intent);
        ((Activity) this).overridePendingTransition(0, 0);
    }

    //launch menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu, menu);
        return false;
    }

    //menu button
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

}
