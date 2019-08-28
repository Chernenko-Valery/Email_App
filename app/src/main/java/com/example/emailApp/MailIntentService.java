package com.example.emailApp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.emailApp.yandex.YandexEmailReader;

/**
 * Класс - наследник IntentService для обработки запросов в фоновом режиме
 */
public class MailIntentService extends IntentService {

    private static final String TAG = "MailIntentService";

    public static final String LOGIN_TAG = "LOGIN";
    public static final String PASSWORD_TAG = "PASSWORD";
    public static final String SERVER_TAG = "SERVER";

    private static final String YANDEX_SERVER = "yandex.com";

    public MailIntentService() {
        super(MailIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent aIntent) {
        Bundle bundle = aIntent.getExtras();
        String login = bundle.getString(LOGIN_TAG);
        String password = bundle.getString(PASSWORD_TAG);
        String server = bundle.getString(SERVER_TAG);
        if (server.equals(YANDEX_SERVER)) {
            YandexEmailReader yandexEmailReader = new YandexEmailReader(login, password);
            int unreadMessageCount = yandexEmailReader.getUnreadMessageCount();
            Log.d(TAG, Integer.toString(unreadMessageCount));
            Intent unreadMessageCountIntent = new Intent(MailReceiver.UNREAD_MESSAGE_COUNT_INTENT_FLAG);
            unreadMessageCountIntent.putExtra(MailReceiver.UNREAD_MESSAGE_COUNT_TAG, unreadMessageCount);
            sendBroadcast(unreadMessageCountIntent);
        }
    }
}
