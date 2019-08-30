package com.example.emailApp;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/** Class Email Authenticator with email and password params */
public class EmailAuthenticator extends Authenticator {

    /** Email*/
    private String mEmail;
    /** Password */
    private String mPassword;

    /**
     * Class constructor specifying email and password
     * @param aEmail - email
     * @param aPassword - password
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
