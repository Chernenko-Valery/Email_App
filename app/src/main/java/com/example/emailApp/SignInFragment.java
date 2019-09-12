package com.example.emailApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emailApp.ImapServerWorker.ImapForegroundService;
import com.example.emailApp.ImapServerWorker.ImapIntentService;

/**
 * Authorization fragment.
 */
public class SignInFragment extends Fragment {

    private static final String TOAST_UNREAD_MESSAGE_COUNT= "Unread message's count = ";
    private static final String TOAST_ERROR = "ERROR: ";

    private static final String SERVER = "yandex.com";

    private static final String APP_PREFERENCES = "emailPreferences";

    private static final String APP_PREFERENCES_LOGIN = "login"; // имя кота
    private static final String APP_PREFERENCES_PASSWORD = "password"; // возраст кота

    /** Field - login EditText. */
    private EditText loginText;
    /** Field - password EditText. */
    private EditText passwordText;

    /**Field - Shared Preferences. */
    private SharedPreferences mPreferences;

    /**
     * Handler for handling message from MailIntentService.
     */
    private final Handler mUiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message aMsg) {
            if(aMsg.what == ImapIntentService.MSG_UNREAD_MESSAGE_COUNT) {
                int unreadMessageCount = aMsg.arg1;
                Toast.makeText(getContext(), TOAST_UNREAD_MESSAGE_COUNT
                            + unreadMessageCount, Toast.LENGTH_LONG).show();
            } else if(aMsg.what == ImapIntentService.MSG_ERROR) {
                String error = (String) aMsg.obj;
                Toast.makeText(getContext(), TOAST_ERROR
                        + error, Toast.LENGTH_LONG).show();
            }
        }
    };

    private Messenger mMessenger = new Messenger(mUiHandler);

    @Override
    public void onCreate(@Nullable Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);
        if(getActivity() != null) {
            mPreferences = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        }
        setRetainInstance(true);
    }

    /** Hide input keyboard. */
    private void hideInput() {
        if(getContext() != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && getView() != null) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater aInflater, @Nullable ViewGroup aContainer,
                             @Nullable Bundle aSavedInstanceState) {
        // Inflate the layout for this fragment
        View view = aInflater.inflate(R.layout.fragment_sign_in, aContainer, false);

        loginText = view.findViewById(R.id.loginText);
        if(mPreferences.contains(APP_PREFERENCES_LOGIN)) {
            loginText.setText(mPreferences.getString(APP_PREFERENCES_LOGIN, ""));
        }

        passwordText = view.findViewById(R.id.passwordText);
        if(mPreferences.contains(APP_PREFERENCES_PASSWORD)) {
            passwordText.setText(mPreferences.getString(APP_PREFERENCES_PASSWORD, ""));
        }

        Button signInButton = view.findViewById(R.id.singInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImapIntentService.class);
                intent.setAction(ImapIntentService.UNREAD_MESSAGE_COUNT_ACTION);
                intent.putExtra(ImapIntentService.EXTRA_LOGIN, loginText.getText().toString());
                intent.putExtra(ImapIntentService.EXTRA_PASSWORD, passwordText.getText().toString());
                intent.putExtra(ImapIntentService.EXTRA_SERVER, SERVER);
                intent.putExtra(ImapIntentService.EXTRA_MESSENGER, mMessenger);
                if(getActivity() != null) {
                    getActivity().startService(intent);
                }

                hideInput();
            }
        });

        Button startForegroundButton = view.findViewById(R.id.startForegroundButton);
        startForegroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImapForegroundService.class);
                intent.putExtra(ImapForegroundService.EXTRA_LOGIN, loginText.getText().toString());
                intent.putExtra(ImapForegroundService.EXTRA_PASSWORD, passwordText.getText().toString());
                intent.putExtra(ImapForegroundService.EXTRA_SERVER, SERVER);
                if(getActivity() != null) {
                    getActivity().startService(intent);
                }

                hideInput();
            }
        });

        Button stopForegroundButton = view.findViewById(R.id.stopForegroundButton);
        stopForegroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImapForegroundService.class);
                if(getActivity() != null) {
                    getActivity().stopService(intent);
                }

                hideInput();
            }
        });
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(APP_PREFERENCES_LOGIN, loginText.getText().toString());
        editor.putString(APP_PREFERENCES_PASSWORD, passwordText.getText().toString());
        editor.apply();
    }
}
