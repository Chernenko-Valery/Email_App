package com.example.emailApp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
