package com.example.emailApp;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/** Класс аутентификатор */
public class EmailAuthenticator extends Authenticator {

    /** Поле почтовый адрес*/
    private String mEmail;
    /** Поле пароль */
    private String mPassword;

    /**
     * Конструктор создание нового объекта с определенными значениями
     * @param aEmail - почтовый адрес
     * @param aPassword - пароль
     */
    public EmailAuthenticator(String aEmail, String aPassword) {
        mEmail = aEmail;
        mPassword = aPassword;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(mEmail, mPassword);
    }
}
