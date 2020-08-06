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
    private Account account;

    private TextView tlTextView;
    private ImageView tlImageView;
    private TextView LHRTextView;
    private ImageView LHRImageView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //overridePendingTransition(0,0);
        Button TL = (Button) findViewById(R.id.TL);
        Button LHR = (Button) findViewById(R.id.LHR);
        account = (Account) getApplication();
        TL.setOnClickListener(v -> {
            account.setTLUsername();
            account.setTLToken();
            account.setTLAccountID();
            Intent intent = new Intent(this,Launch.class);
            startActivity(intent);
            //clear activity startUI
            ((Activity) this).overridePendingTransition(0, 0);
        });
        LHR.setOnClickListener(v -> {
            account.setLHRUsername();
            account.setLHRToken();
            account.setLHRAccountID();
            Intent intent = new Intent(this,Launch.class);
            startActivity(intent);
            //clear activity startUI
            ((Activity) this).overridePendingTransition(0, 0);
        });
    }





    //launch menu
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu, menu);

        return false;
    }
    //menu button
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean onOptionsItemSelected(MenuItem item)
    {

        TextView tlTextView = (TextView) findViewById(R.id.nameTL);
        ImageView tlImageView = (ImageView) findViewById(R.id.imageTL);
        TextView LHRTextView = (TextView) findViewById(R.id.textView);
        ImageView LHRImageView = (ImageView) findViewById(R.id.imageView2);

        //MenuItem TL = (MenuItem) findViewById(R.id.flavor);
        //Button function
//        switch (item.getItemId())
//        {
//            case R.id.test1:
////
//
//                //Button remove = findViewById(R.id.action_remove);
//                tlTextView.setVisibility(View.GONE);
//                tlImageView.setVisibility(View.GONE);
//                LHRTextView.setVisibility(View.GONE);
//                LHRImageView.setVisibility(View.GONE);
//
////                TL.(View.GONE);
//
//                break;
//            case R.id.test2:
////                事件
//                tlTextView.setVisibility(View.VISIBLE);
//                tlImageView.setVisibility(View.VISIBLE);
//                LHRTextView.setVisibility(View.VISIBLE);
//                LHRImageView.setVisibility(View.VISIBLE);
////                Intent intent = new Intent(this,Login.class);
////                startActivity(intent);
//                break;
//        }
        return true;
    }



}
