package com.example.emailApp;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emailApp.ImapServerWorker.ImapIntentService;

/**
 * Authorization fragment.
 */
public class SignInFragment extends Fragment {

    private static final String TOAST_UNREAD_MESSAGE_COUNT= "Unread message's count = ";
    private static final String TOAST_ERROR = "ERROR: ";

    private static final String SERVER = "yandex.com";

    private EditText loginText;
    private EditText passwordText;

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

    private Messenger messenger = new Messenger(mUiHandler);

    @Override
    public void onCreate(@Nullable Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater aInflater, @Nullable ViewGroup aContainer,
                             @Nullable Bundle aSavedInstanceState) {
        // Inflate the layout for this fragment
        View view = aInflater.inflate(R.layout.fragment_sign_in, aContainer, false);
        Button signInButton = view.findViewById(R.id.singInButton);
        loginText = view.findViewById(R.id.loginText);
        passwordText = view.findViewById(R.id.passwordText);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImapIntentService.class);
                intent.setAction(ImapIntentService.UNREAD_MESSAGE_COUNT_ACTION);
                intent.putExtra(ImapIntentService.EXTRA_LOGIN, loginText.getText().toString());
                intent.putExtra(ImapIntentService.EXTRA_PASSWORD, passwordText.getText().toString());
                intent.putExtra(ImapIntentService.EXTRA_SERVER, SERVER);
                intent.putExtra(ImapIntentService.EXTRA_MESSENGER, messenger);
                if(getActivity() != null) {
                    getActivity().startService(intent);
                }

                //Hide input
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        return view;
    }

}
