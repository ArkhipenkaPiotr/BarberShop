package com.arkhipenka.android.barbershop.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.arkhipenka.android.barbershop.App;
import com.arkhipenka.android.barbershop.Entities.BarberUser;
import com.arkhipenka.android.barbershop.Entities.Entry;
import com.arkhipenka.android.barbershop.Entities.Hairdresser;
import com.arkhipenka.android.barbershop.Entities.Service;
import com.arkhipenka.android.barbershop.Entities.WorkingHours;
import com.arkhipenka.android.barbershop.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arkhipenka_piotr on 09.12.2017.
 */

public class EntryPickerFragment extends Fragment {
    private static final String HAIRDRESSER_KEY = "hairdresser_key";
    private static final String WORKING_HOURS_KEY = "working_hours_key";

    DatePicker datePicker;
    TimePicker timePicker;
    Spinner spinner;
    Button saveButton;
    List<String> spinnerItems;
    Hairdresser hairdresser;
    WorkingHours workingHours;
    List<Entry> entries;
    BarberUser barberUser;
    List<Service> services;

    public static EntryPickerFragment newInstance(Hairdresser hairdresser, WorkingHours workingHours) {

        Bundle args = new Bundle();
        args.putSerializable(HAIRDRESSER_KEY,hairdresser);
        args.putSerializable(WORKING_HOURS_KEY,workingHours);

        EntryPickerFragment fragment = new EntryPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entry_picker,container,false);
        datePicker = v.findViewById(R.id.fragment_entry_picker_date);
        spinner = v.findViewById(R.id.fragment_entry_picker_spinner);
        saveButton = v.findViewById(R.id.fragment_entry_picker_button);
        timePicker = v.findViewById(R.id.fragment_entry_picker_time);

        timePicker.setIs24HourView(true);

        hairdresser = (Hairdresser) getArguments().getSerializable(HAIRDRESSER_KEY);
        workingHours = (WorkingHours) getArguments().getSerializable(WORKING_HOURS_KEY);

        initEntries();
        initBarberUser();
        initSpinner();
        initSaveButton();

        return v;
    }

    private void initSpinner(){
        App.getApi().getServices().enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                if (response.body()!=null) {
                    services = new ArrayList<Service>();
                    services.addAll(response.body());
                    spinnerItems = new ArrayList<String>();
                    for (Service service : response.body()){
                        spinnerItems.add(service.getName()+" ("+service.getPrice()+" BYN)");
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, spinnerItems);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(arrayAdapter);
                    spinner.setSelection(0);
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initEntries(){
        App.getApi().getHairdresserEntriesOf(hairdresser.getId()).enqueue(new Callback<List<Entry>>() {
            @Override
            public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                entries = response.body();
            }

            @Override
            public void onFailure(Call<List<Entry>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initBarberUser(){
        App.getApi().getUser(App.getAccessable().getId()).enqueue(new Callback<BarberUser>() {
            @Override
            public void onResponse(Call<BarberUser> call, Response<BarberUser> response) {
                barberUser = response.body();
            }

            @Override
            public void onFailure(Call<BarberUser> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initSaveButton(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDateCorrectly()){
                    Toast.makeText(getContext(), "Check the date correctness", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!isEntryNotExist()){
                        Toast.makeText(getContext(), "This time is already taken", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        DateTimeZone zone = DateTimeZone.forID( "America/Montreal" );
                        DateTime selectedDate = new DateTime(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),timePicker.getCurrentHour(),timePicker.getCurrentMinute(),zone);

                        Service service = services.get(spinner.getSelectedItemPosition());
                        Entry entry = new Entry(hairdresser,barberUser,selectedDate.toDate(),service);
                        App.getApi().addEntry(entry).enqueue(new Callback<Entry>() {
                            @Override
                            public void onResponse(Call<Entry> call, Response<Entry> response) {
                                getFragmentManager().popBackStack();
                                Toast.makeText(getContext(), "Good", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Entry> call, Throwable t) {
                               Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private boolean isDateCorrectly(){
        DateTimeZone zone = DateTimeZone.forID( "America/Montreal" );
        DateTime now = DateTime.now( zone );
        DateTime selectedDate = new DateTime(datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth(),timePicker.getCurrentHour(),timePicker.getCurrentMinute(),zone);

        return now.isBefore(selectedDate);
    }

    private boolean isEntryNotExist(){
        Date date = new Date(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),timePicker.getCurrentHour(),timePicker.getCurrentMinute());
        for(Entry entry:entries){
            if (Math.abs(entry.getServiceTime().getTime()-date.getTime())<1000*60*30){
                return false;
            }
        }
        return true;
    }
}
