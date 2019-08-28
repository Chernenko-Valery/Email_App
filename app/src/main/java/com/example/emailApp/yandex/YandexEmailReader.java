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
    private Session getSession() {
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
    public Integer getUnreadMessageCount() {
        try {
            Session session = getSession();

            Store store = session.getStore();
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
