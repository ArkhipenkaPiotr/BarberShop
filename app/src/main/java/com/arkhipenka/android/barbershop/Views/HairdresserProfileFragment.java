package com.arkhipenka.android.barbershop.Views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arkhipenka.android.barbershop.Adapters.RecyclerViewEntriesAdapter;
import com.arkhipenka.android.barbershop.App;
import com.arkhipenka.android.barbershop.Entities.Accessable;
import com.arkhipenka.android.barbershop.Entities.Entry;
import com.arkhipenka.android.barbershop.Entities.Hairdresser;
import com.arkhipenka.android.barbershop.Entities.WorkingHours;
import com.arkhipenka.android.barbershop.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arkhipenka_piotr on 17.11.2017.
 */

public class HairdresserProfileFragment extends Fragment {

    private static final String HAIRDRESSER_KEY = "hairdresser";
    private static final String PHOTO_KEY = "photoImageView";

    Hairdresser hairdresser;
    WorkingHours workingHours;
    List<Entry> entryList;

    ImageView photoImageView;
    EditText lastNameEditText;
    EditText firstNameEditText;
    ImageView maleImageView;
    ImageView femaleImageVIew;
    EditText mondayEditText;
    EditText tuesdayEditText;
    EditText wednesdayEditText;
    EditText thursdayEditText;
    EditText fridayEditText;
    EditText saturdayEditText;
    EditText sundayEditText;
    RecyclerView entriesRecyclerView;
    Button buttonAddEntry;
    Button buttonDelete;
    Button saveButton;
    ProgressBar progressBar;

    public static Fragment newInstance(@NonNull long hairdresserId){
        Bundle bundle = new Bundle();

        bundle.putLong(HAIRDRESSER_KEY,hairdresserId);

        Fragment fragment = new HairdresserProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hairdresser_profile,container,false);

        photoImageView = v.findViewById(R.id.fragment_hairdresser_profile_image);
        lastNameEditText = v.findViewById(R.id.fragment_hairdresser_profile_last_name);
        firstNameEditText = v.findViewById(R.id.fragment_hairdresser_profile_first_name);
        maleImageView = v.findViewById(R.id.fragment_hairdresser_profile_man_icon);
        femaleImageVIew = v.findViewById(R.id.fragment_hairdresser_profile_woman_icon);
        mondayEditText = v.findViewById(R.id.fragment_hairdresser_profile_mon);
        tuesdayEditText = v.findViewById(R.id.fragment_hairdresser_profile_tue);
        wednesdayEditText = v.findViewById(R.id.fragment_hairdresser_profile_wed);
        thursdayEditText = v.findViewById(R.id.fragment_hairdresser_profile_thu);
        fridayEditText = v.findViewById(R.id.fragment_hairdresser_profile_fri);
        saturdayEditText = v.findViewById(R.id.fragment_hairdresser_profile_sat);
        sundayEditText = v.findViewById(R.id.fragment_hairdresser_profile_sun);
        entriesRecyclerView = v.findViewById(R.id.fragment_hairdresser_profile_entries);
        buttonAddEntry = v.findViewById(R.id.fragment_hairdresser_profile_add_entry);
        buttonDelete = v.findViewById(R.id.fragment_hairdresser_profile_delete_hairdresser);
        progressBar = v.findViewById(R.id.fragment_hairdresser_profile_progress_bar);
        saveButton = v.findViewById(R.id.fragment_hairdresser_profile_save_hairdresser);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getApi().deleteHairdresser(hairdresser.getId()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(getContext(),"Hairdresser was deleted", Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getContext(),"Delete error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        buttonAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.getAccessable()==null||App.getAccessable().getPermissions().equals(Accessable.TYPE_HAIRDRESSER)){
                    Toast.makeText(getContext(), "Войдите в систему!", Toast.LENGTH_LONG).show();
                }
                else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer,EntryPickerFragment.newInstance(hairdresser,workingHours))
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
        return v;
    }


    private void saveChanges(){
        progressBar.setVisibility(View.VISIBLE);

        if (hairdresser!=null) {
            hairdresser.setLastName(lastNameEditText.getText().toString());
            hairdresser.setFirstName(firstNameEditText.getText().toString());
        }
        if (workingHours!=null) {
            workingHours.setMonday(mondayEditText.getText().toString());
            workingHours.setTuesday(tuesdayEditText.getText().toString());
            workingHours.setWednesday(wednesdayEditText.getText().toString());
            workingHours.setThursday(thursdayEditText.getText().toString());
            workingHours.setFriday(fridayEditText.getText().toString());
            workingHours.setSaturday(saturdayEditText.getText().toString());
            workingHours.setSunday(sundayEditText.getText().toString());
        }

        App.getApi().addHairdresser(hairdresser).enqueue(new Callback<Hairdresser>() {
            @Override
            public void onResponse(Call<Hairdresser> call, Response<Hairdresser> response) {
                Toast.makeText(getContext(),"Изменения сохранены", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Hairdresser> call, Throwable t) {
                Toast.makeText(getContext(),"Ошибка", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        entriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fillHairdresser();
        //hairdresser = new Hairdresser(1,null,null,null,null,null);
    }

    private void fillWorkingHours(){
        if (hairdresser!=null) {
            App.getApi().getWorkingHoursOf(hairdresser.getId()).enqueue(new Callback<WorkingHours>() {
                @Override
                public void onResponse(Call<WorkingHours> call, Response<WorkingHours> response) {
                    workingHours = response.body();
                    if (workingHours.getMonday()==null){
                        mondayEditText.setHint(getString(R.string.no_work));
                    }
                    else {
                        mondayEditText.setText(workingHours.getMonday());
                    }
                    if (workingHours.getTuesday()==null){
                        tuesdayEditText.setHint(getString(R.string.no_work));
                    }
                    else {
                        tuesdayEditText.setText(workingHours.getTuesday());
                    }
                    if (workingHours.getWednesday()==null){
                        wednesdayEditText.setHint(getString(R.string.no_work));
                    }
                    else {
                        wednesdayEditText.setText(workingHours.getWednesday());
                    }
                    if (workingHours.getThursday()!=null) {
                        thursdayEditText.setText(workingHours.getThursday());
                    }
                    else {
                        thursdayEditText.setHint(getString(R.string.no_work));
                    }
                    if (workingHours.getFriday()!=null) {
                        fridayEditText.setText(workingHours.getFriday());
                    }
                    else{
                        fridayEditText.setHint(getString(R.string.no_work));
                    }
                    if (workingHours.getSaturday()!=null) {
                        saturdayEditText.setText(workingHours.getSaturday());
                    }
                    else {
                        saturdayEditText.setHint(getString(R.string.no_work));
                    }
                    if (workingHours.getSunday()!=null) {
                        sundayEditText.setText(workingHours.getSunday());
                    }
                    else{
                        sundayEditText.setHint(getString(R.string.no_work));
                    }
                }

                @Override
                public void onFailure(Call<WorkingHours> call, Throwable t) {
                    Toast.makeText(getContext(), "Рабочие часы не загружены", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    private void fillEntries(){
        if (hairdresser!=null) {
            App.getApi().getHairdresserEntriesOf(hairdresser.getId()).enqueue(new Callback<List<Entry>>() {
                @Override
                public void onResponse(@NonNull Call<List<Entry>> call, @NonNull Response<List<Entry>> response) {
                    entryList = response.body();
                    entriesRecyclerView.setAdapter(new RecyclerViewEntriesAdapter(entryList));
                    entriesRecyclerView.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<Entry>> call, Throwable t) {
                    Toast.makeText(getContext(), "Записи к парикмахеру не были загружены ", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    private void fillHairdresser(){

        App.getApi().getHairdresser(getArguments().getLong(HAIRDRESSER_KEY)).enqueue(new Callback<Hairdresser>() {
            @Override
            public void onResponse(Call<Hairdresser> call, Response<Hairdresser> response) {
                if (response.body()!=null) {
                    hairdresser = response.body();

                    lastNameEditText.setText(hairdresser.getLastName());
                    firstNameEditText.setText(hairdresser.getFirstName());
                    if (hairdresser.getClientGender().contains(Hairdresser.SPECIALITY_MAN)) {
                        maleImageView.setVisibility(View.VISIBLE);
                    }
                    if (hairdresser.getClientGender().contains(Hairdresser.SPECIALITY_WOMAN)) {
                        femaleImageVIew.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);

                    if (App.getAccessable()==null||(!App.getAccessable().getPermissions().equals(Accessable.TYPE_ADMIN)&&(!(App.getAccessable().getPermissions().equals(Accessable.TYPE_HAIRDRESSER)&&App.getAccessable().getId()==hairdresser.getId())))) {
                        lastNameEditText.setEnabled(false);
                        firstNameEditText.setEnabled(false);
                        mondayEditText.setEnabled(false);
                        tuesdayEditText.setEnabled(false);
                        wednesdayEditText.setEnabled(false);
                        thursdayEditText.setEnabled(false);
                        fridayEditText.setEnabled(false);
                        saturdayEditText.setEnabled(false);
                        sundayEditText.setEnabled(false);

                        buttonDelete.setVisibility(View.GONE);
                        saveButton.setVisibility(View.GONE);
                    }

                    fillWorkingHours();
                    fillEntries();
                    fillImageView();
                }
            }

            @Override
            public void onFailure(Call<Hairdresser> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка загрузки", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillImageView(){
        if (hairdresser!=null){
            Picasso.with(getContext())
                    .load(hairdresser.getPhotoUrl())
                    .into(photoImageView);
        }
    }
}
