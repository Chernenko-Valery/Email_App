package com.example.email_app.yandex;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.email_app.EmailAuthenticator;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class YandexEmailReader {

    private static final String YANDEX_ERROR_LOG = "YANDEX_ERROR";
    private static final String YANDEX_DEBUG_LOG = "YANDEX_DEBUG";

    private static final String SERVER = "yandex.com";
    private static final String IMAP_SERVER = "imap." + SERVER;
    private static final Integer IMAP_PORT = 993;

    public static final int SESSION_NULL_ERROR_CODE = -1;
    public static final int NO_SUCH_PROVIDER_ERROR_CODE = -2;
    public static final int MESSAGING_ERROR_CODE = -3;

    private String email;
    private String password;

    private Session session = null;


    public YandexEmailReader(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean init_session() {

        if(session != null)
            return true;

        //Создание свойств подключения
        Properties properties =  new Properties();
        properties.put("mail.debug", "false");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.port", Integer.toString(IMAP_PORT));

        Authenticator authenticator = new EmailAuthenticator(email, password);

        session = Session.getDefaultInstance(properties, authenticator);
        session.setDebug(false);
        return true;
    }

    public Integer getUnreadMessageCount() {
        if(session==null) {
            Log.d(YANDEX_ERROR_LOG, "Session is null");
            return SESSION_NULL_ERROR_CODE;
        }
        try {
            Store store = session.getStore();
            store.connect(IMAP_SERVER, email, password);
            Log.d(YANDEX_DEBUG_LOG, "Store is connected");

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Log.d(YANDEX_DEBUG_LOG, "INBOX Folder in opened");

            //Получение числа непрочитанных сообщений
            return inbox.getUnreadMessageCount();

        } catch (NoSuchProviderException nspEx) {
            Log.d(YANDEX_ERROR_LOG, nspEx.getMessage());
            return NO_SUCH_PROVIDER_ERROR_CODE;
        } catch (MessagingException mEx) {
            Log.d(YANDEX_ERROR_LOG, mEx.getMessage());
            return MESSAGING_ERROR_CODE;
        }
    }
}
