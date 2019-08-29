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
 * Фрагмент содержащий поля для ввода логина и пароля, а также кнопку, по которой осуществляется вход
 */
public class SignInFragment extends Fragment {

    /** Пустой конструктор */
    public SignInFragment() {
        // Required empty public constructor
    }

    /** Поле - логин */
    EditText mLogin;
    /** Поле - пароль */
    EditText mPassword;

    /**
     * Поле - Handler для обработки сообщений от MailIntentService
     */
    Handler mUiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message aMsg) {
            if(aMsg.what == MailIntentService.UNREAD_MESSAGE_COUNT_WHAT_TAG) {
                if(aMsg.arg1 >= 0) {
                    Toast toast = Toast.makeText(getContext(), "Количество непрочитанных сообщений = " + aMsg.arg1, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getContext(), "ошибка с кодом: " + aMsg.arg1, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    };

    /**
     * Поле - Messenger для взаимодействия с MailIntentService
     */
    Messenger mMessenger = new Messenger(mUiHandler);

    @Override
    public View onCreateView(final LayoutInflater aInflater, ViewGroup aContainer,
                             Bundle aSavedInstanceState) {
        // Inflate the layout for this fragment
        View view = aInflater.inflate(R.layout.fragment_sign_in, aContainer, false);
        mLogin = view.findViewById(R.id.loginText);
        mPassword = view.findViewById(R.id.passwordText);
        Button signInButton = view.findViewById(R.id.singInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MailIntentService.class);
                intent.putExtra(MailIntentService.LOGIN_TAG, mLogin.getText().toString() + "@yandex.com");
                intent.putExtra(MailIntentService.PASSWORD_TAG, mPassword.getText().toString());
                intent.putExtra(MailIntentService.SERVER_TAG, "yandex.com");
                intent.putExtra(MailIntentService.MESSENGER_TAG, mMessenger);
                getActivity().startService(intent);
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
