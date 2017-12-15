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
import android.widget.TextView;
import android.widget.Toast;

import com.arkhipenka.android.barbershop.App;
import com.arkhipenka.android.barbershop.Entities.Entry;
import com.arkhipenka.android.barbershop.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arkhipenka_piotr on 07.12.2017.
 */

public class UsersEntriesFragment extends Fragment {
    private static final String ID_KEY = "id_key";

    RecyclerView recyclerView;
    Long userId;
    List<Entry> entries;
    Button button;

    public static UsersEntriesFragment newInstance(Long id) {
        Bundle args = new Bundle();
        args.putLong(ID_KEY,id);

        UsersEntriesFragment fragment = new UsersEntriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users_entries,container,false);
        userId = getArguments().getLong(ID_KEY);

        button = v.findViewById(R.id.fragment_users_entries_log_out);
        recyclerView = v.findViewById(R.id.fragment_users_entries_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        entries = new ArrayList<>();
        recyclerView.setAdapter(new EntriesAdapter(entries));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.setAccessable(null);
                getFragmentManager().popBackStack();
            }
        });
        App.getApi().getEntriesOf(userId).enqueue(new Callback<List<Entry>>() {
            @Override
            public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                entries.addAll(response.body());
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Entry>> call, Throwable t) {
                Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    private class EntriesAdapter extends RecyclerView.Adapter<EntriesHolder> {
        private List<Entry> entryList;


        public EntriesAdapter(List<Entry> entryList) {
            this.entryList = entryList;
        }


        @Override
        public EntriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entries,parent,false);

            return new EntriesHolder(v);
        }

        @Override
        public void onBindViewHolder(EntriesHolder holder, int position) {
            holder.bindEntries(entryList.get(position));
        }


        @Override
        public int getItemCount() {
            return entryList.size();
        }
    }

    private class EntriesHolder extends RecyclerView.ViewHolder{
        TextView dateTextView;
        TextView barberTextView;

        public EntriesHolder(View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.item_entries_date);
            barberTextView = itemView.findViewById(R.id.item_entries_user);
        }

        public void bindEntries(Entry entry){
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd/MM");
            String date = sdf.format(entry.getServiceTime());
            dateTextView.setText(date);
            barberTextView.setText(entry.getHairdresser().getLastName());
        }
    }
}
