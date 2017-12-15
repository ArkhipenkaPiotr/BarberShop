package com.arkhipenka.android.barbershop.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arkhipenka.android.barbershop.App;
import com.arkhipenka.android.barbershop.Entities.BarberUser;
import com.arkhipenka.android.barbershop.Entities.Hairdresser;
import com.arkhipenka.android.barbershop.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arkhipenka_piotr on 14.11.2017.
 */

public class ValidationFragment extends Fragment {

    EditText loginEditText;
    EditText passwordEditText;
    Button enterButton;
    Button registrationButton;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_validation,container,false);

        progressBar = v.findViewById(R.id.fragment_validation_progress_bar);
        loginEditText = v.findViewById(R.id.fragment_validation_login_edit_text);
        passwordEditText = v.findViewById(R.id.fragment_validation_password_edit_text);
        enterButton = v.findViewById(R.id.fragment_validation_enter_button);
        registrationButton = v.findViewById(R.id.fragment_validation_registration_button);

        getActivity().openOptionsMenu();

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                progressBar.setVisibility(View.VISIBLE);

                if (login.contains("_staff")) {
                    login = login.split("_")[0];
                    App.getApi().getHairdresser(login,password).enqueue(new Callback<Hairdresser>() {
                        @Override
                        public void onResponse(Call<Hairdresser> call, Response<Hairdresser> response) {
                            Hairdresser hairdresser = response.body();
                            if (hairdresser!=null){
                                App.setAccessable(hairdresser);
                                Toast.makeText(getContext(),"Welcome to account",Toast.LENGTH_SHORT).show();
                                getFragmentManager().popBackStack();
                            }
                        }

                        @Override
                        public void onFailure(Call<Hairdresser> call, Throwable t) {

                        }
                    });
                }
                else {
                    App.getApi().getBarberUser(login, password).enqueue(new Callback<BarberUser>() {
                        @Override
                        public void onResponse(Call<BarberUser> call, Response<BarberUser> response) {
                            progressBar.setVisibility(View.GONE);

                            if (response.body() != null) {
                                App.setAccessable(response.body());
                                Toast.makeText(getContext(), "Welcome to account", Toast.LENGTH_SHORT).show();
                                getFragmentManager().popBackStack();
                            } else {
                                passwordEditText.setText("");
                                Toast.makeText(getContext(), "Wrong login or password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BarberUser> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Wrong login or password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                progressBar.setVisibility(View.VISIBLE);

                App.getApi().userRegistration(login,password).enqueue(new Callback<BarberUser>() {
                    @Override
                    public void onResponse(Call<BarberUser> call, Response<BarberUser> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.body()!=null){
                            App.setAccessable(response.body());
                            Toast.makeText(getContext(), "Welcome to account", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                        }
                        else {
                            Toast.makeText(getContext(), "User with this login is already exist", Toast.LENGTH_SHORT).show();
                            passwordEditText.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<BarberUser> call, Throwable t) {
                        Toast.makeText(getContext(), "Connection error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return v;
    }
}
