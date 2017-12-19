package com.arkhipenka.android.barbershop.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arkhipenka.android.barbershop.App;
import com.arkhipenka.android.barbershop.Entities.Hairdresser;
import com.arkhipenka.android.barbershop.Entities.WorkingHours;
import com.arkhipenka.android.barbershop.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arkhipenka_piotr on 07.12.2017.
 */

public class CreateHairdresserFragment extends Fragment {
    private Hairdresser hairdresser;
    private EditText photoUrlEditText;
    private EditText lastNameEditText;
    private EditText firstNameEditText;
    private EditText clientGenderEditText;
    private EditText passwordEditText;
    private Button saveButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_hairdresser,container,false);
        photoUrlEditText = v.findViewById(R.id.fragment_create_hairdresser_photo_url);
        lastNameEditText = v.findViewById(R.id.fragment_create_hairdresser_lastname);
        firstNameEditText = v.findViewById(R.id.fragment_create_hairdresser_firstname);
        clientGenderEditText = v.findViewById(R.id.fragment_create_hairdresser_client_gender);
        passwordEditText = v.findViewById(R.id.fragment_create_hairdresser_password);
        saveButton = v.findViewById(R.id.fragment_create_hairdresser_save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveHairdresser();
            }
        });
        return v;
    }

    private void saveHairdresser(){
        hairdresser = new Hairdresser();

        WorkingHours workingHours = new WorkingHours();
        hairdresser.setPhotoUrl(photoUrlEditText.getText().toString());
        hairdresser.setLastName(lastNameEditText.getText().toString());
        hairdresser.setFirstName(firstNameEditText.getText().toString());
        hairdresser.setClientGender(clientGenderEditText.getText().toString());
        hairdresser.setPassword(passwordEditText.getText().toString());

        App.getApi().addHairdresser(hairdresser).enqueue(new Callback<Hairdresser>() {
            @Override
            public void onResponse(Call<Hairdresser> call, Response<Hairdresser> response) {
                Toast.makeText(getContext(),"Парикмахер добавлен", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Hairdresser> call, Throwable t) {
                Toast.makeText(getContext(),"Парикмахер не добавлен", Toast.LENGTH_LONG).show();
            }
        });

        App.getApi().addWorkingHours(workingHours).enqueue(new Callback<WorkingHours>() {
            @Override
            public void onResponse(Call<WorkingHours> call, Response<WorkingHours> response) {

            }

            @Override
            public void onFailure(Call<WorkingHours> call, Throwable t) {

            }
        });
    }
}
