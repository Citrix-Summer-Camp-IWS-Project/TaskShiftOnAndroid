package com.citrix.taskshiftonandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Launch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingview);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Splash spThread = new Splash();
        spThread.start();
    }

    class Splash extends Thread {
        public void run() {
            //get account from Login
            Account account = (Account) getIntent().getSerializableExtra("Account");
            //initialize new intent of list
            Intent intent = new Intent(Launch.this, MainActivity.class);
            ArrayList<Item> items = new ArrayList<Item>();
            try {
                items = getAllIssueInfo(account.getUsername(), account.getToken());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            intent.putExtra("items", items);
            intent.putExtra("account", account);
            startActivity(intent);
            ((Activity) Launch.this).overridePendingTransition(0, 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public ArrayList<Item> getAllIssueInfo(final String username, String token) throws InterruptedException {
        final ArrayList<Item> itemList = new ArrayList<Item>();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        final CountDownLatch latch = new CountDownLatch(1);
        final String KEY = "MD";
        String credential = Credentials.basic(username, token);
        HttpUrl httpUrl = HttpUrl.parse("https://nj-summer-camp-2020.atlassian.net/rest/api/3/search").newBuilder()
                .addQueryParameter("jql", "project=" + KEY)
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .method("GET", null)
                .addHeader("Authorization", credential)
                .addHeader("Cookie", "atlassian.xsrf.token=3b8b59a3-a91d-43ab-91e9-1f39c1f730a8_5b1c7d1bbfe800ba2d5af1baeed5078f6ccf7d4d_lin")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Web No Response.");
                latch.countDown();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArr = (JSONArray) jsonObject.get("issues");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        Item issue = new Item(jsonObj, username);
                        if (issue.emailAddress.equals(username)) {
                            itemList.add(issue);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        });
        latch.await();
        return itemList;
    }

}
