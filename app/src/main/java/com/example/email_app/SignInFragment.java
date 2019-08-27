package com.example.email_app;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.email_app.yandex.YandexEmailReader;

//Фрагмент авторизации
public class SignInFragment extends Fragment {


    public SignInFragment() {
        // Required empty public constructor
    }

    EditText login;
    EditText password;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        login = view.findViewById(R.id.loginText);
        password = view.findViewById(R.id.passwordText);
        Button signInButton = view.findViewById(R.id.singInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MailService.class);
                intent.putExtra(MailService.LOGIN_TAG, login.getText().toString() + "@yandex.com");
                intent.putExtra(MailService.PASSWORD_TAG, password.getText().toString());
                intent.putExtra(MailService.SERVER_TAG, "yandex.com");
                getActivity().startService(intent);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
