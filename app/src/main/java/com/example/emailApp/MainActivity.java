package com.example.emailApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MailReceiver mMailReceiver = new MailReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mMailReceiver, new IntentFilter(MailReceiver.UNREAD_MESSAGE_COUNT_INTENT_FLAG));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMailReceiver);
    }
}
