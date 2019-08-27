package com.example.emailApp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.emailApp.yandex.YandexEmailReader;

public class MailService extends IntentService {

    private static final String TAG = "MailService";

    public static final String LOGIN_TAG = "LOGIN";
    public static final String PASSWORD_TAG = "PASSWORD";
    public static final String SERVER_TAG = "SERVER";

    private static final String YANDEX_SERVER = "yandex.com";


    public MailService() {
        super(MailService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String login = bundle.getString(LOGIN_TAG);
        String password = bundle.getString(PASSWORD_TAG);
        String server = bundle.getString(SERVER_TAG);
        if (server.equals(YANDEX_SERVER)) {
            YandexEmailReader yandexEmailReader = new YandexEmailReader(login, password);
            if (yandexEmailReader.initSession()) {
                int unreadMessageCount = yandexEmailReader.getUnreadMessageCount();
                Log.d(TAG, Integer.toString(unreadMessageCount));
                Intent unreadMessageCountIntent = new Intent(MailReceiver.UNREAD_MESSAGE_COUNT_INTENT_FLAG);
                unreadMessageCountIntent.putExtra(MailReceiver.UNREAD_MESSAGE_COUNT_TAG, unreadMessageCount);
                sendBroadcast(unreadMessageCountIntent);
            }
        }
    }
}
