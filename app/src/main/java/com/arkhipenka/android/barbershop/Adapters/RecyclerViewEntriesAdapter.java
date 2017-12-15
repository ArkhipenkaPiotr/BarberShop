package com.arkhipenka.android.barbershop.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arkhipenka.android.barbershop.App;
import com.arkhipenka.android.barbershop.Entities.Entry;
import com.arkhipenka.android.barbershop.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arkhipenka_piotr on 16.11.2017.
 */

public class RecyclerViewEntriesAdapter extends RecyclerView.Adapter<RecyclerViewEntriesAdapter.EntriesHolder> {
    private List<Entry> entryList;


    public RecyclerViewEntriesAdapter(List<Entry> entryList) {
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

    public class EntriesHolder extends RecyclerView.ViewHolder{
        TextView dateTextView;
        TextView userTextView;

        public EntriesHolder(View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.item_entries_date);
            userTextView = itemView.findViewById(R.id.item_entries_user);
        }

        public void bindEntries(Entry entry){
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd/MM");
            String date = sdf.format(entry.getServiceTime());
            dateTextView.setText(date);
            userTextView.setText(entry.getBarberUser().getLogin());
        }
    }
}
