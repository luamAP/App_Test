package com.example.app_test;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        WebView webView = (WebView)
                findViewById(R.id.webview);
        webView.loadUrl("https://youtu.be/tL1o60lHaho");

    }

}
