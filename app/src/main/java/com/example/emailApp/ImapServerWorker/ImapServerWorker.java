package com.example.emailApp.ImapServerWorker;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.emailApp.EmailAuthenticator;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

/** Abstract class that interactive with imap mail server */
public class ImapServerWorker {

    /** Debug tag */
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

    /** Field - Server address without @. */
    private String mServer;
    /** Field - Login without server. */
    private String mLogin;
    /** Field - Password. */
    private String mPassword;

    ImapServerWorker(@Nullable String aLogin, @Nullable String aPassword, @Nullable String aServer) {
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
     * Method that return new Store objects with parameters in class object
     *
     * @return new Session
     * @throws MessagingException if fail
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
    private Folder getFolder(@NonNull String aFolderName, boolean aReadOnlyFlag) throws MessagingException {
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
