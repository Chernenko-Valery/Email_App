package com.example.emailApp.ImapServerWorker;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import javax.mail.MessagingException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class ImapIntentService extends IntentService {

    private static final String TAG = "ImapIntentService";

    public static final String UNREAD_MESSAGE_COUNT_ACTION =
            "com.example.emailapp.ImapServerWorker.action.UNREAD_MESSAGE_COUNT";

    public static final int MSG_UNREAD_MESSAGE_COUNT = 1;
    public static final int MSG_ERROR = 2;

    public static final String EXTRA_LOGIN = "LOGIN";
    public static final String EXTRA_PASSWORD = "PASSWORD";
    public static final String EXTRA_SERVER = "SERVER";
    public static final String EXTRA_MESSENGER = "MESSENGER";

    public ImapIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if(UNREAD_MESSAGE_COUNT_ACTION.equals(action)) {
                final Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    final String login = bundle.getString(EXTRA_LOGIN);
                    final String password = bundle.getString(EXTRA_PASSWORD);
                    final String server = bundle.getString(EXTRA_SERVER);

                    final ImapServerWorker imapServerWorker = new ImapServerWorker(login, password, server);
                    final Message m = new Message();
                    try {
                        final int unreadMessageCountPair = imapServerWorker.getUnreadMessageCount();
                        Log.d(TAG, "Unread message's count = " + unreadMessageCountPair);
                        m.what = MSG_UNREAD_MESSAGE_COUNT;
                        m.arg1 = unreadMessageCountPair;
                    } catch (MessagingException aE) {
                        Log.d(TAG, "Error: " + aE.getMessage());
                        m.what = MSG_ERROR;
                        m.obj = aE.getMessage();
                    }
                    final Messenger messenger = bundle.getParcelable(EXTRA_MESSENGER);
                    try {
                        if (messenger != null) {
                            messenger.send(m);
                        }
                    } catch (RemoteException aE) {
                        Log.d(TAG, "Error: " + aE.getMessage());
                    }
                }
            }
        }
    }
}
