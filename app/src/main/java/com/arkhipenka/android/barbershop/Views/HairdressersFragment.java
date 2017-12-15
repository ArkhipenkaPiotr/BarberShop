package com.arkhipenka.android.barbershop.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arkhipenka.android.barbershop.App;
import com.arkhipenka.android.barbershop.Entities.Accessable;
import com.arkhipenka.android.barbershop.Entities.Hairdresser;
import com.arkhipenka.android.barbershop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arkhipenka_piotr on 14.11.2017.
 */

public class HairdressersFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<Hairdresser> hairdressers;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hairdressers,container,false);
        hairdressers = new ArrayList<>();

        progressBar = v.findViewById(R.id.fragment_container_progressbar);

        recyclerView = v.findViewById(R.id.fragment_hairdressers_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewHairdresserAdapter hairdresserAdapter = new RecyclerViewHairdresserAdapter(hairdressers);
        recyclerView.setAdapter(hairdresserAdapter);

        App.getApi().getHairdressers().enqueue(new Callback<List<Hairdresser>>() {
            @Override
            public void onResponse(Call<List<Hairdresser>> call, Response<List<Hairdresser>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    hairdressers.addAll(response.body());
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Hairdresser>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private class RecyclerViewHairdresserAdapter extends RecyclerView.Adapter<RecyclerViewHairdresserAdapter.HairdresserHolder> {

        private List<Hairdresser> hairdressers;

        private RecyclerViewHairdresserAdapter(List<Hairdresser> hairdressers) {
            this.hairdressers = hairdressers;

        }

        @Override
        public HairdresserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hairdresser,parent,false);


            return new HairdresserHolder(view);
        }

        @Override
        public void onBindViewHolder(HairdresserHolder holder, int position) {
            holder.bindHairdresser(hairdressers.get(position));
        }

        @Override
        public int getItemCount() {
            if (hairdressers==null){
                return 0;
            }
            return hairdressers.size();
        }

        class HairdresserHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private ImageView imageView;
            private ImageView manImageView;
            private ImageView womanImageView;
            private TextView firstNameTextView;
            private TextView lastNameTextView;
            private Hairdresser hairdresser;

            public HairdresserHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                manImageView = itemView.findViewById(R.id.item_hairdresser_man_image);
                womanImageView = itemView.findViewById(R.id.item_hairdresser_woman_image);
                imageView = itemView.findViewById(R.id.item_hairdresser_image_view);
                firstNameTextView = itemView.findViewById(R.id.item_hairdresser_first_name);
                lastNameTextView = itemView.findViewById(R.id.item_hairdresser_last_name);
            }

            public void bindHairdresser(Hairdresser hairdresser){
                //загрузить фотку
                this.hairdresser = hairdresser;
                Picasso.with(getContext())
                        .load(hairdresser.getPhotoUrl())
                        .into(imageView);
                firstNameTextView.setText(hairdresser.getFirstName());
                lastNameTextView.setText(hairdresser.getLastName());
                if (hairdresser.getClientGender().contains(Hairdresser.SPECIALITY_MAN)){
                    manImageView.setVisibility(View.VISIBLE);
                }
                if (hairdresser.getClientGender().contains(Hairdresser.SPECIALITY_WOMAN)){
                    womanImageView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,HairdresserProfileFragment.newInstance(hairdresser.getId()))
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //((ViewGroup)recyclerView.getParent()).removeAllViewsInLayout();
    }
}
