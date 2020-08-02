package com.citrix.taskshiftonandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;


public class Launch extends AppCompatActivity {

    public static Activity Launch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingview);
        Launch = this;
        Handler handler = new Handler();

        System.out.println("before start 1activity");

        handler.postDelayed(new splashhandler(), 200);//延迟执行splashhandler线程
    }

    class splashhandler implements Runnable{
        public void run() {
            //launch main activity
            System.out.println("before start activity");
            startActivity(new Intent(getApplication(),MainActivity.class));
            //Launch.this.finish();// 把当前的LaunchActivity结束掉
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


}