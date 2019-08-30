package com.example.emailApp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.example.emailApp.yandex.YandexEmailReader;

/**
 * Intent Service for processing requests
 */
public class MailIntentService extends IntentService {

    private static final String TAG = "MailIntentService";

    public static final String LOGIN_TAG = "LOGIN";
    public static final String PASSWORD_TAG = "PASSWORD";
    public static final String SERVER_TAG = "SERVER";
    public static final String MESSENGER_TAG = "BINDER";

    public static final int UNREAD_MESSAGE_COUNT_WHAT_TAG = 1;

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
        if(bundle!=null) {
            String login = bundle.getString(LOGIN_TAG);
            String password = bundle.getString(PASSWORD_TAG);
            String server = bundle.getString(SERVER_TAG);
            Messenger messenger = bundle.getParcelable(MESSENGER_TAG);
            if (server!=null && server.equals(YANDEX_SERVER)) {
                YandexEmailReader yandexEmailReader = new YandexEmailReader(login, password);
                int unreadMessageCount = yandexEmailReader.getUnreadMessageCount(yandexEmailReader.getSession());
                Log.d(TAG, Integer.toString(unreadMessageCount));
                //Intent unreadMessageCountIntent = new Intent(MailReceiver.UNREAD_MESSAGE_COUNT_INTENT_FLAG);
                //unreadMessageCountIntent.putExtra(MailReceiver.UNREAD_MESSAGE_COUNT_TAG, unreadMessageCount);
                //sendBroadcast(unreadMessageCountIntent);
                Message msg = new Message();
                msg.what = UNREAD_MESSAGE_COUNT_WHAT_TAG;
                msg.arg1 = unreadMessageCount;
                try {
                    if (messenger != null) messenger.send(msg);
                } catch (RemoteException aE) {
                    if (aE.getMessage() != null) Log.d(TAG, aE.getMessage());
                }
            }
        }
    }
}
