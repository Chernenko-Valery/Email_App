package com.example.emailApp.yandex;

import android.content.Intent;
import android.util.Log;

import com.example.emailApp.EmailAuthenticator;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Класс, осуществяющий взаимодействие пользователя с imap сервером Яндекс Почты
 */
public class YandexEmailReader {

    private static final String TAG = "YandexEmailReader";

    private static final String SERVER = "yandex.com";
    private static final String IMAP_SERVER = "imap." + SERVER;
    private static final Integer IMAP_PORT = 993;

    /** Коды ошибок */
    public static final int NO_SUCH_PROVIDER_ERROR_CODE = -1;
    public static final int MESSAGING_ERROR_CODE = -2;

    /** Поле почтовый адрес */
    private String mEmail;
    /** Поле пароль */
    private String mPassword;

    /**
     * Констуктор - создание нового объекта с определенными значениями
     * @param aEmail - почтовый адрес
     * @param aPassword - пароль
     */
    public YandexEmailReader(String aEmail, String aPassword) {
        mEmail = aEmail;
        mPassword = aPassword;
    }

    /**
     * Функция создания нового объекта класса Session по заданным параметрам в объекте
     * @return возвращает новый объект класса Session
     */
    public Session getSession() {
        //Создание свойств подключения
        Properties properties =  new Properties();
        properties.put("mail.debug", "false");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.port", Integer.toString(IMAP_PORT));

        Authenticator authenticator = new EmailAuthenticator(mEmail, mPassword);

        Session session = Session.getDefaultInstance(properties, authenticator);
        session.setDebug(false);
        return session;
    }

    /**
     * Функция получения количесва непрочитанных входящих сообщений
     * @return возвращает количество непрочитанных входящих сообщений в случае успеха,
     * @return иначе возвращает один из кодов ошибки класса YandexEmailReader
     */
    public Integer getUnreadMessageCount(Session aSession) {
        try {
            Store store = aSession.getStore();
            store.connect(IMAP_SERVER, mEmail, mPassword);
            Log.d(TAG, "Store is connected");

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Log.d(TAG, "INBOX Folder in opened");

            /*
            Message[] messages = inbox.getMessages();
            for (int i =0; i < messages.length; i++) {
                Log.d(TAG, messages[i].getSubject());
                Log.d(TAG, getText(messages[i]));
            }*/

            //Получение числа непрочитанных сообщений
            return inbox.getUnreadMessageCount();
        } catch (NoSuchProviderException nspEx) {
            Log.d(TAG, nspEx.getMessage());
            return NO_SUCH_PROVIDER_ERROR_CODE;
        } catch (MessagingException mEx) {
            Log.d(TAG, mEx.getMessage());
            return MESSAGING_ERROR_CODE;
        }/* catch (IOException e) {
            e.printStackTrace();
            return -55555;
        }*/
    }

    /**
     * Return the primary text content of the message.
     */
    private String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }
}
