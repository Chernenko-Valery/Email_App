package com.example.emailApp.yandex;

import android.util.Log;
import android.util.Pair;

import com.example.emailApp.EmailAuthenticator;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * A class that implements user interaction with the Yandex mail imap server
 */
public class YandexEmailReader {

    private static final String TAG = "YandexEmailReader";

    private static final String SERVER = "yandex.com";
    private static final String IMAP_SERVER = "imap." + SERVER;
    private static final Integer IMAP_PORT = 993;

    private static final String INBOX = "INBOX";

    /** Email */
    private final String mEmail;
    /** Password */
    private final String mPassword;

    /**
     * Class constructor specifying user's email and password
     * @param aEmail - email
     * @param aPassword - password
     */
    public YandexEmailReader(String aEmail, String aPassword) {
        mEmail = aEmail;
        mPassword = aPassword;
    }

    /**
     * Method that returns a new Session object according to the parameters specified in the class
     * @return new Session object
     */
    public Session getSession() {
        //Создание свойств подключения
        final Properties properties =  new Properties();
        properties.put("mail.debug", "false");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.port", Integer.toString(IMAP_PORT));

        Authenticator authenticator = new EmailAuthenticator(mEmail, mPassword);
        Log.d(TAG, ((EmailAuthenticator) authenticator).getmEmail());
        Log.d(TAG, ((EmailAuthenticator) authenticator).getmPassword());

        Session session = Session.getInstance(properties, authenticator);
        session.setDebug(false);
        return session;
    }

    /**
     * Method that returns count of unread messages in user's email
     * @return Pair<Integer, String> that first is count of unread message if successful
     * or -1 otherwise, second is empty if successful or error message otherwise
     */
    public Pair<Integer, String> getUnreadMessageCount(Session aSession) {
        try {
            Log.d(TAG, aSession.toString());

            Store store = aSession.getStore();
            store.connect(IMAP_SERVER, mEmail, mPassword);
            Log.d(TAG, "Store is connected");

            Folder inbox = store.getFolder(INBOX);
            inbox.open(Folder.READ_ONLY);
            Log.d(TAG, INBOX + " Folder in opened");

            return new Pair<>(inbox.getUnreadMessageCount(), "");
        } catch (NoSuchProviderException aE) {
            if(aE.getMessage()!=null) Log.d(TAG, aE.getMessage());
            return new Pair<>(-1, aE.getMessage());
        } catch (MessagingException aE) {
            if(aE.getMessage()!=null) Log.d(TAG, aE.getMessage());
            //TODO sending other message if other problems
            return new Pair<>(-1, aE.getMessage());
        }
    }

    /*
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
    }*/
}
