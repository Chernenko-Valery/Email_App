package com.example.email_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MailReciever mailReciever = new MailReciever();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mailReciever, new IntentFilter(MailReciever.UNREAD_MESSAGE_COUNT_INTENT_FLAG));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mailReciever);
    }
}
