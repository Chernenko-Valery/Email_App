package com.example.emailApp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Fragment that containing fields for entering a login and password, and a button for entering
 */
public class SignInFragment extends Fragment {

    /** Class constructor */
    public SignInFragment() {
        // Required empty public constructor
    }

    private static final String UNREAD_MESSAGE_COUNT_MESSAGE = "Count of undead messages = ";
    private static final String ERROR_MESSAGE = "Error with code: ";

    /** EditText with login without server */
    private EditText mLogin;
    /** EditText with password */
    private EditText mPassword;

    /**
     * Handler for handling message from MailIntentService
     */
    private final Handler mUiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message aMsg) {
            if(aMsg.what == MailIntentService.UNREAD_MESSAGE_COUNT_WHAT_TAG) {
                if(aMsg.arg1 >= 0) {
                    Toast.makeText(getContext(), UNREAD_MESSAGE_COUNT_MESSAGE
                            + aMsg.arg1, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), ERROR_MESSAGE
                            + aMsg.arg1, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * Messenger for interaction with MailIntentService
     */
    private final Messenger mMessenger = new Messenger(mUiHandler);

    @Override
    public View onCreateView(final @NonNull LayoutInflater aInflater, ViewGroup aContainer,
                             Bundle aSavedInstanceState) {
        // Inflate the layout for this fragment
        View view = aInflater.inflate(R.layout.fragment_sign_in, aContainer, false);
        mLogin = view.findViewById(R.id.loginText);
        mPassword = view.findViewById(R.id.passwordText);
        final Button signInButton = view.findViewById(R.id.singInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MailIntentService.class);
                intent.putExtra(MailIntentService.LOGIN_TAG, mLogin.getText().toString() + "@yandex.com");
                intent.putExtra(MailIntentService.PASSWORD_TAG, mPassword.getText().toString());
                intent.putExtra(MailIntentService.SERVER_TAG, "yandex.com");
                intent.putExtra(MailIntentService.MESSENGER_TAG, mMessenger);
                if(getActivity()!=null) getActivity().startService(intent);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);
        setRetainInstance(true);
    }
}
