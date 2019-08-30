package com.example.emailApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

/**
 * BroadcastReceiver for receiving message from MainIntentService
 */
public class MailReceiver extends BroadcastReceiver {

    private static final String TAG = "MailReceiver";

    public static final String ACTION_UNREAD_MESSAGE_COUNT = "com.example.email_app.UNREAD_MESSAGE_COUNT_INTENT_FLAG";
    public static final String EXTRA_MESSAGE_COUNT_TAG = "EXTRA_MESSAGE_COUNT";

    private static final String UNREAD_MESSAGE_COUNT_MESSAGE = "Count of unread messages = ";
    private static final String ERROR_MESSAGE = "Error with code: ";


    @Override
    public void onReceive(Context aContext, Intent aIntent) {
        if(aIntent.getAction()!=null && aIntent.getAction().equals(ACTION_UNREAD_MESSAGE_COUNT)) {
            if(aIntent.getExtras()!=null) {
                int unreadMessageCount = aIntent.getExtras().getInt(EXTRA_MESSAGE_COUNT_TAG);
                if(unreadMessageCount >= 0) {
                    Toast.makeText(aContext, UNREAD_MESSAGE_COUNT_MESSAGE
                            + unreadMessageCount, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(aContext, ERROR_MESSAGE
                            + unreadMessageCount, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
