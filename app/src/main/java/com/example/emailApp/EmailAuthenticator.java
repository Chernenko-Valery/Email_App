package com.example.emailApp;

import androidx.annotation.NonNull;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/** Class Email Authenticator with email and password params. */
public final class EmailAuthenticator extends Authenticator {

    /** Email. */
    final private String mEmail;
    /** Password. */
    final private String mPassword;

    /**
     * Class constructor specifying email and password.
     * @param aEmail - email.
     * @param aPassword - password.
     */
    public EmailAuthenticator(@NonNull final String aEmail, @NonNull final String aPassword) {
        mEmail = aEmail;
        mPassword = aPassword;
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(mEmail, mPassword);
    }
}
