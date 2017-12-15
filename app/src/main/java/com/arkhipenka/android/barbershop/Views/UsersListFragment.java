package com.arkhipenka.android.barbershop.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arkhipenka.android.barbershop.App;
import com.arkhipenka.android.barbershop.Entities.Accessable;
import com.arkhipenka.android.barbershop.Entities.BarberUser;
import com.arkhipenka.android.barbershop.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arkhipenka_piotr on 07.12.2017.
 */

public class UsersListFragment extends Fragment {
    RecyclerView recyclerView;
    List<BarberUser> barberUsers;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users_list,container,false);
        recyclerView = v.findViewById(R.id.fragment_users_list_recycler_view);
        progressBar = v.findViewById(R.id.fragment_users_list_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        //сменить адаптер
        barberUsers = new ArrayList<>();
        recyclerView.setAdapter(new UsersListAdapter(barberUsers));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        App.getApi().getUsers().enqueue(new Callback<List<BarberUser>>() {
            @Override
            public void onResponse(Call<List<BarberUser>> call, Response<List<BarberUser>> response) {
                if (response.body()!=null) {
                    barberUsers.addAll(response.body());
                    recyclerView.getAdapter().notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<BarberUser>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    private class UsersListAdapter extends RecyclerView.Adapter<UsersListHolder>{
        List<BarberUser> barberUsers;

        public UsersListAdapter(List<BarberUser> barberUsers) {
            this.barberUsers = barberUsers;
        }

        @Override
        public UsersListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.item_user,parent,false);
            return new UsersListHolder(v);
        }

        @Override
        public void onBindViewHolder(UsersListHolder holder, int position) {
            holder.bindUser(barberUsers.get(position));
        }

        @Override
        public int getItemCount() {
            return barberUsers.size();
        }
    }

    private class UsersListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        Button button;
        BarberUser barberUser;

        public UsersListHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_user_username);
            button = itemView.findViewById(R.id.item_user_permissions);
        }

        public void bindUser(final BarberUser barberUser){
            textView.setText(barberUser.getLogin());
            if (barberUser.getPermissions().equals(Accessable.TYPE_ADMIN)){
                button.setText(getResources().getString(R.string.delete_from_admin_s));
            }
            else{
                button.setText(getResources().getString(R.string.add_to_admin_s_list));
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (barberUser.getPermissions().equals(Accessable.TYPE_ADMIN)){
                        button.setText(getResources().getString(R.string.add_to_admin_s_list));
                        barberUser.setPermissions(Accessable.TYPE_HAIRDRESSER);
                    }
                    else{
                        button.setText(getResources().getString(R.string.delete_from_admin_s));
                        barberUser.setPermissions(Accessable.TYPE_ADMIN);
                    }
                    saveBarberUser(barberUser);
                }
            });
        }

        private void saveBarberUser(BarberUser barberUser){
            App.getApi().saveUser(barberUser).enqueue(new Callback<BarberUser>() {
                @Override
                public void onResponse(Call<BarberUser> call, Response<BarberUser> response) {
                    Toast.makeText(getContext(),"Changes saved",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<BarberUser> call, Throwable t) {
                    Toast.makeText(getContext(),"Error! Changes not saved",Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View view) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,UsersEntriesFragment.newInstance(barberUser.getId()))
                    .addToBackStack(null)
                    .commit();
        }
    }
}
