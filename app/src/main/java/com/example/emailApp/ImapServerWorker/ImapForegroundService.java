package com.example.emailApp.ImapServerWorker;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import javax.mail.MessagingException;

public class ImapForegroundService extends Service {

    private static final String TAG = "ImapForegroundService";

    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CONTENT_TITLE = "YANDEX MAIL";
    private static final String NOTIFICATION_CONTENT_TEXT = "START FOREGROUND";

    public static final String EXTRA_LOGIN = "LOGIN";
    public static final String EXTRA_PASSWORD = "PASSWORD";
    public static final String EXTRA_SERVER = "SERVER";

    /** Field - Main Notification Manager. */
    private NotificationManager mNotificationManager;
    /** Field - Main Notification Builder. */
    private NotificationCompat.Builder mBuilder;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle(NOTIFICATION_CONTENT_TITLE).setContentText(NOTIFICATION_CONTENT_TEXT);
        startForeground(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Override
    public int onStartCommand(final Intent aIntent, final int aFlags, final int aStartId) {
        if (aIntent!=null) {
            Log.d(TAG,"onStartCommand");
            final String login = aIntent.getStringExtra(EXTRA_LOGIN);
            final String password = aIntent.getStringExtra(EXTRA_PASSWORD);
            final String server = aIntent.getStringExtra(EXTRA_SERVER);
            final ImapServerWorker imapServerWorker = new ImapServerWorker(login, password, server);
            new Thread() {
                @Override
                public void run() {
                    try {
                        imapServerWorker.startNotifyMessageCountListenerOnInbox(mBuilder, mNotificationManager, NOTIFICATION_ID);
                    } catch (MessagingException aE) {
                        Log.d(TAG, "" + aE.getMessage());
                    }
                }
            }.start();
        }
        return super.onStartCommand(aIntent, aFlags, aStartId);
    }

    @Override
    public IBinder onBind(Intent aIntent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
