package com.example.emailApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Класс - наследник BroadcastReceiver для получения сообщений
 */
public class MailReceiver extends BroadcastReceiver {

    public static final String UNREAD_MESSAGE_COUNT_INTENT_FLAG = "com.example.email_app.UNREAD_MESSAGE_COUNT_INTENT_FLAG";
    public static final String UNREAD_MESSAGE_COUNT_TAG = "UNREAD_MESSAGE_COUNT";

    @Override
    public void onReceive(Context aContext, Intent aIntent) {
        if(aIntent.getAction().equals(UNREAD_MESSAGE_COUNT_INTENT_FLAG)) {
            int unreadMessageCount = aIntent.getExtras().getInt(UNREAD_MESSAGE_COUNT_TAG);
            if(unreadMessageCount >= 0) {
                Toast toast = Toast.makeText(aContext, "Количество непрочитанных сообщений = " + unreadMessageCount, Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(aContext, "ошибка с кодом: " + unreadMessageCount, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
