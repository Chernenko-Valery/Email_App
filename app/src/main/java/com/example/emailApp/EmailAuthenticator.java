package com.example.emailApp;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailAuthenticator extends Authenticator {

    private String mLogin;
    private String mPassword;

    public EmailAuthenticator(String login, String password) {
        mLogin = login;
        mPassword = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(mLogin, mPassword);
    }
}
