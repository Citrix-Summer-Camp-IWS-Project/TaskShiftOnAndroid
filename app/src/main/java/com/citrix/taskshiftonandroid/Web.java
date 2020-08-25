package com.citrix.taskshiftonandroid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Web extends AppCompatActivity {

    public static String[] tokens = new String[2];
    private WebView webView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        try {
            setWebView();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setWebView() throws InterruptedException {
        Account account = (Account) getIntent().getSerializableExtra("Account");
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(account.getUrl());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("https://www.baidu.com")) {
                    String code = getCode(url);

                    try {
                        String[] tokenList = getAccessToken(code, account);
                        tokens[0] = tokenList[0];
                        tokens[1] = tokenList[1];
                    } catch (InterruptedException | JSONException e) {
                        e.printStackTrace();
                    }
                    webView.destroy();
                    Intent intent = new Intent(Web.this, Launch.class);
                    intent.putExtra("Account", account);
                    startActivity(intent);
                }
                return false;
            }
        });

        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
    }

    public String getCode(String url) {
        Uri uri = Uri.parse(url);
        String code = uri.getQueryParameter("code");
        return code;
    }

    public String[] getAccessToken(String code, Account account) throws InterruptedException, JSONException {
        final String[] token = new String[2];
        final CountDownLatch latch = new CountDownLatch(1);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("grant_type", "authorization_code");
        jsonObject.put("client_id", account.getClientID());
        jsonObject.put("client_secret", account.getClientSecret());
        jsonObject.put("code", code);
        jsonObject.put("redirect_uri", "https://baidu.com");
        RequestBody body = RequestBody.create(jsonObject.toString(), mediaType);
        Request request = new Request.Builder()
                .url("https://auth.atlassian.com/oauth/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "did=s%3Av0%3A8df3ab00-db29-11ea-bf89-c35a1fc9b7c9.F1d%2BQqm%2FNR1jUhuBEDIAKFABFoiGuoQkTSfnGCcVfJo; did_compat=s%3Av0%3A8df3ab00-db29-11ea-bf89-c35a1fc9b7c9.F1d%2BQqm%2FNR1jUhuBEDIAKFABFoiGuoQkTSfnGCcVfJo")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("this is fail");
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject jsonObj = new JSONObject(response.body().string());
                    token[0] = (String) jsonObj.get("access_token");
                    token[1] = (String) jsonObj.get("refresh_token");
                    latch.countDown();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        latch.await();

        return token;
    }

    public static String getAccessToken(){
        return tokens[0];
    }

    public static String getRefreshToken(){
        return tokens[1];
    }
}