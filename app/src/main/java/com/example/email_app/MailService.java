package com.example.email_app;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.email_app.yandex.YandexEmailReader;

public class MailService extends IntentService {

    private static final String MAIL_DEBUG_LOG = "MAIL_DEBUG";

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
            if (yandexEmailReader.init_session()) {
                int unreadMessageCount = yandexEmailReader.getUnreadMessageCount();
                Log.d(MAIL_DEBUG_LOG, Integer.toString(unreadMessageCount));
                Intent unreadMessageCountIntent = new Intent(MailReciever.UNREAD_MESSAGE_COUNT_INTENT_FLAG);
                unreadMessageCountIntent.putExtra(MailReciever.UNREAD_MESSAGE_COUNT_TAG, unreadMessageCount);
                sendBroadcast(unreadMessageCountIntent);
            }
        }
    }
}
