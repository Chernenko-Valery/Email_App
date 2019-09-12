package com.example.emailApp.ImapServerWorker;

import android.app.NotificationManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.emailApp.EmailAuthenticator;
import com.sun.mail.imap.IMAPFolder;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

/** Abstract class that interactive with imap mail server. */
public class ImapServerWorker {

    /** Debug tag. */
    private static final String TAG = "ImapServerWorker";

    private static final String IMAP_PREFIX = "imap.";
    private static final String IMAP_PORT_TAG = "mail.imap.port";
    private static final String IMAP_PORT = "993";

    private static final String MAIL_DEBUG_TAG = "mail.debug";
    private static final boolean MAIL_DEBUG_FLAG = false;

    private static final String MAIL_STORE_PROTOCOL_TAG = "mail.store.protocol";
    private static final String MAIL_STORE_PROTOCOL_VALUE = "imaps";

    private static final String MAIL_IMAP_SSL_ENABLE_TAG = "mail.imap.ssl.enable";
    private static final boolean MAIL_IMAP_SSL_ENABLE_FLAG = true;

    private static final String INBOX_FOLDER_NAME = "INBOX";

    private static final int NEW_MESSAGE_NOTIFICATION_ID = 2;
    private static final String NEW_MESSAGE_NOTIFICATION_CONTENT_TITLE = "New Message";
    private static final String NOTIFICATION_CONTENT_TEXT = "Unread Message's count = ";

    private static final long UPDATE_NOTIFICATION_SLEEP_TIME = 1000 * 30;

    /** Field - Server address without @. */
    private String mServer;
    /** Field - Login without server. */
    private String mLogin;
    /** Field - Password. */
    private String mPassword;

    /** Field - Idle Thread */
    private Thread idleThread;
    /** Field - update Thread */
    private Thread updateThread;

    ImapServerWorker(@Nullable final String aLogin, @Nullable final String aPassword, @Nullable final String aServer) {
        this.mLogin = aLogin;
        this.mPassword = aPassword;
        this.mServer = aServer;
    }

    /**
     * Method that return new Session objects with parameters in class object.
     *
     * @return new Session.
     */
    private Session getSession() {
        final Properties properties = new Properties();
        properties.put(IMAP_PORT_TAG, IMAP_PORT);
        properties.put(MAIL_DEBUG_TAG, Boolean.toString(MAIL_DEBUG_FLAG));
        properties.put(MAIL_STORE_PROTOCOL_TAG, MAIL_STORE_PROTOCOL_VALUE);
        properties.put(MAIL_IMAP_SSL_ENABLE_TAG, Boolean.toString(MAIL_IMAP_SSL_ENABLE_FLAG));

        final Authenticator authenticator = new EmailAuthenticator(mLogin, mPassword);

        final Session session = Session.getInstance(properties, authenticator);
        Log.d(TAG, "Session is created");
        session.setDebug(false);
        return session;
    }

    /**
     * Method that return new Store objects with parameters in class object.
     *
     * @return new Session.
     * @throws MessagingException if fail.
     */
    private Store getStore() throws MessagingException {
        final Session session = getSession();

        final Store store = session.getStore();
        store.connect(IMAP_PREFIX + mServer, mLogin + "@" + mServer, mPassword);
        Log.d(TAG, "Store is connected");
        return store;
    }

    /**
     * Method that return new Folder objects.
     *
     * @param aFolderName - Folder Name.
     * @param aReadOnlyFlag - Read_only flag (true - read_only, else read_write).
     * @return new Folder.
     * @throws MessagingException if fail.
     */
    private Folder getFolder(@NonNull final String aFolderName, final boolean aReadOnlyFlag) throws MessagingException {
        final Store store = getStore();

        final Folder folder = store.getFolder(aFolderName);
        if (aReadOnlyFlag) {
            folder.open(Folder.READ_ONLY);
        } else {
            folder.open(Folder.READ_WRITE);
        }
        Log.d(TAG, aFolderName + " Folder is opened in " + (aReadOnlyFlag ? "READ_ONLY" : "READ_WRITE") + " mode");
        return folder;
    }

    /**
     * Method that start Notify Message Count Listener idle on Inbox folder in new Thread.
     *
     * @param aBuilder - Main Builder for creating Unread message's count's adding Notification.
     * @param aNotificationManager - Main Notification manager for building all Notification.
     * @param aNotificationId - Unread message's count's adding Notification Id.
     * @throws MessagingException is fail.
     */
    public void startNotifyMessageCountListenerOnInbox(@NonNull final NotificationCompat.Builder aBuilder,
                                                       @NonNull final NotificationManager aNotificationManager,
                                                       final int aNotificationId) throws MessagingException {
        final IMAPFolder folder = (IMAPFolder) getFolder(INBOX_FOLDER_NAME, false);
        final NotificationCompat.Builder newMessageBuilder = new NotificationCompat.Builder(aBuilder.mContext);
        newMessageBuilder.setSmallIcon(android.R.drawable.ic_dialog_email);
        newMessageBuilder.setContentTitle(NEW_MESSAGE_NOTIFICATION_CONTENT_TITLE);

        folder.addMessageCountListener(new MessageCountAdapter() {
            @Override
            public void messagesAdded(MessageCountEvent aEvent) {
                try {
                    Log.d(TAG, "New message incoming.");
                    aBuilder.setContentText(NOTIFICATION_CONTENT_TEXT + folder.getUnreadMessageCount());
                    aNotificationManager.notify(aNotificationId, aBuilder.build());
                    newMessageBuilder.setContentText(aEvent.getMessages()[0].getSubject());
                    aNotificationManager.notify(NEW_MESSAGE_NOTIFICATION_ID, newMessageBuilder.build());
                } catch (MessagingException aE) {
                    Log.d(TAG, "" + aE.getMessage());
                }
            }

            @Override
            public void messagesRemoved(MessageCountEvent aEvent) {
                super.messagesRemoved(aEvent);
                try {
                Log.d(TAG, "New message deleted.");
                    aBuilder.setContentText(NOTIFICATION_CONTENT_TEXT + folder.getUnreadMessageCount());
                    aNotificationManager.notify(aNotificationId, aBuilder.build());
                } catch (MessagingException aE) {
                    Log.d(TAG, "" + aE.getMessage());
                }
            }
        });

        if(idleThread != null) {
            idleThread.interrupt();
        }
        idleThread = new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "Start Idle");
                try {
                    while (folder.isOpen() || !isInterrupted()) {
                        folder.idle();
                    }
                } catch (MessagingException aE) {
                    Log.d(TAG, "" + aE.getMessage());
                }
            }
        };
        idleThread.start();

        if(updateThread != null) {
            updateThread.interrupt();
        }
        updateThread = new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "Start Update");
                try {
                    while(folder.isOpen() || !isInterrupted()) {
                        Log.d(TAG, "Update");
                        aBuilder.setContentText(NOTIFICATION_CONTENT_TEXT + folder.getUnreadMessageCount());
                        aNotificationManager.notify(aNotificationId, aBuilder.build());
                        SystemClock.sleep(UPDATE_NOTIFICATION_SLEEP_TIME);
                    }
                } catch (MessagingException aE) {
                    Log.d(TAG, aE.getMessage());
                }
            }
        };
        updateThread.start();
    }

    /**
     * Method that returns count of unread messages in user's email.
     *
     * @return unread message's count.
     * @exception MessagingException if fail.
     */
    public int getUnreadMessageCount() throws MessagingException{
        final Folder folder = getFolder(INBOX_FOLDER_NAME, true);
        return folder.getUnreadMessageCount();
    }

}
