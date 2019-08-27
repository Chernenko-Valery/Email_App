package com.example.email_app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

public class MailReciever extends BroadcastReceiver {

    public static final String UNREAD_MESSAGE_COUNT_INTENT_FLAG = "com.example.email_app.UNREAD_MESSAGE_COUNT_INTENT_FLAG";
    public static final String UNREAD_MESSAGE_COUNT_TAG = "UNREAD_MESSAGE_COUNT";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(UNREAD_MESSAGE_COUNT_INTENT_FLAG)) {
            int unreadMessageCount = intent.getExtras().getInt(UNREAD_MESSAGE_COUNT_TAG);
            if(unreadMessageCount >= 0) {
                Toast toast = Toast.makeText(context, "Количество непрочитанных сообщений = " + unreadMessageCount, Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(context, "ошибка с кодом: " + unreadMessageCount, Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(context, "Ошибка создания сессии", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
