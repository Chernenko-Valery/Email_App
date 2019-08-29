package com.example.emailApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ///** Поле - приёмщик сообщений */
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
