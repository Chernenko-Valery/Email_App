package com.example.emailApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    ///** BroadcastReceiver */
    //MailReceiver mMailReceiver = new MailReceiver();

    @Override
    protected void onCreate(Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //registerReceiver(mMailReceiver, new IntentFilter(MailReceiver.UNREAD_MESSAGE_COUNT_INTENT_FLAG));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(mMailReceiver);
    }
}
