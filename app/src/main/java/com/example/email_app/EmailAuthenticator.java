package com.example.email_app;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailAuthenticator extends Authenticator {

    private String login;
    private String password;

    public EmailAuthenticator(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(login, password);
    }
}
