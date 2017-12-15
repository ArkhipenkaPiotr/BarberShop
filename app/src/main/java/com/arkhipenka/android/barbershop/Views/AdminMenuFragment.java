package com.arkhipenka.android.barbershop.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arkhipenka.android.barbershop.App;
import com.arkhipenka.android.barbershop.Entities.Hairdresser;
import com.arkhipenka.android.barbershop.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arkhipenka_piotr on 06.12.2017.
 */

public class AdminMenuFragment extends Fragment {

    private Button addHairdresserButton;
    private Button usersListButton;
    private Button serviceListButton;
    private Button statisticsButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_menu,container,false);
        addHairdresserButton = v.findViewById(R.id.fragment_admin_menu_add);
        usersListButton = v.findViewById(R.id.fragment_admin_menu_users_list);
        serviceListButton = v.findViewById(R.id.fragment_admin_menu_service_list);
        statisticsButton = v.findViewById(R.id.fragment_admin_menu_statistics);

        addHairdresserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,new CreateHairdresserFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        usersListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,new UsersListFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        serviceListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,new ServiceListFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,new StatisticsFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return v;
    }
}
