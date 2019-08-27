package com.example.emailApp.yandex;

import android.util.Log;

import com.example.emailApp.EmailAuthenticator;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class YandexEmailReader {

    private static final String TAG = "YandexEmailReader";

    private static final String SERVER = "yandex.com";
    private static final String IMAP_SERVER = "imap." + SERVER;
    private static final Integer IMAP_PORT = 993;

    public static final int SESSION_NULL_ERROR_CODE = -1;
    public static final int NO_SUCH_PROVIDER_ERROR_CODE = -2;
    public static final int MESSAGING_ERROR_CODE = -3;

    private String mEmail;
    private String mPassword;

    private Session mSession = null;


    public YandexEmailReader(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    public boolean initSession() {

        //Создание свойств подключения
        Properties properties =  new Properties();
        properties.put("mail.debug", "false");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.port", Integer.toString(IMAP_PORT));

        Authenticator authenticator = new EmailAuthenticator(mEmail, mPassword);

        mSession = Session.getDefaultInstance(properties, authenticator);
        mSession.setDebug(false);
        return true;
    }

    public Integer getUnreadMessageCount() {
        if(mSession==null) {
            Log.d(TAG, "Session is null");
            return SESSION_NULL_ERROR_CODE;
        }
        try {
            Store store = mSession.getStore();
            store.connect(IMAP_SERVER, mEmail, mPassword);
            Log.d(TAG, "Store is connected");

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Log.d(TAG, "INBOX Folder in opened");

            //Получение числа непрочитанных сообщений
            return inbox.getUnreadMessageCount();

        } catch (NoSuchProviderException nspEx) {
            Log.d(TAG, nspEx.getMessage());
            return NO_SUCH_PROVIDER_ERROR_CODE;
        } catch (MessagingException mEx) {
            Log.d(TAG, mEx.getMessage());
            return MESSAGING_ERROR_CODE;
        }
    }
}
