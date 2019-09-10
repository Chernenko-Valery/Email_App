package com.example.emailApp;

import androidx.annotation.NonNull;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/** Class Email Authenticator with email and password params. */
public class EmailAuthenticator extends Authenticator {

    /** Email. */
    private String mEmail;
    /** Password. */
    private String mPassword;

    /**
     * Class constructor specifying email and password
     * @param aEmail - email
     * @param aPassword - password
     */
    public EmailAuthenticator(@NonNull String aEmail,@NonNull String aPassword) {
        mEmail = aEmail;
        mPassword = aPassword;
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(mEmail, mPassword);
    }
}
