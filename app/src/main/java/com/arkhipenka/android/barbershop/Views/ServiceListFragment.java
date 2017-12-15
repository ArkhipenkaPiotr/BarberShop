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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arkhipenka.android.barbershop.App;
import com.arkhipenka.android.barbershop.Entities.Service;
import com.arkhipenka.android.barbershop.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arkhipenka_piotr on 08.12.2017.
 */

public class ServiceListFragment extends Fragment {
    List<Service> serviceList;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ServiceAdapter serviceHolderAdapter;
    EditText addNameEditText;
    EditText addPriceEditText;
    Button addServiceButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_list,container,false);

        recyclerView = v.findViewById(R.id.fragment_service_list_recycler_view);
        progressBar = v.findViewById(R.id.fragment_service_list_progress_bar);
        addNameEditText = v.findViewById(R.id.fragment_service_name_text_view);
        addPriceEditText = v.findViewById(R.id.fragment_service_price_text_view);
        addServiceButton = v.findViewById(R.id.fragment_service_add_button);

        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Service service = new Service();
                service.setName(addNameEditText.getText().toString());
                service.setPrice(Double.valueOf(addPriceEditText.getText().toString()));
                serviceList.add(service);
                recyclerView.getAdapter().notifyDataSetChanged();
                saveService(service);
            }
        });

        serviceList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        serviceHolderAdapter = new ServiceAdapter(serviceList);
        recyclerView.setAdapter(serviceHolderAdapter);

        App.getApi().getServices().enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                progressBar.setVisibility(View.GONE);
                serviceList.addAll(response.body());
                serviceHolderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    private class ServiceAdapter extends RecyclerView.Adapter<ServiceHolder>{
        List<Service> serviceList;

        public ServiceAdapter(List<Service> serviceist) {
            this.serviceList = serviceist;
        }

        @Override
        public ServiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.item_service,parent,false);
            return new ServiceHolder(v);
        }

        @Override
        public void onBindViewHolder(ServiceHolder holder, int position) {
            holder.bindService(serviceList.get(position));
        }

        @Override
        public int getItemCount() {
            return serviceList.size();
        }
    }

    private class ServiceHolder extends RecyclerView.ViewHolder {
        private EditText nameEditText;
        private EditText priceEditText;
        private Button deleteButton;
        private Button saveButton;
        private Service service;

        public ServiceHolder(View itemView) {
            super(itemView);
            nameEditText = itemView.findViewById(R.id.item_service_name_text_view);
            priceEditText = itemView.findViewById(R.id.item_service_price_text_view);
            deleteButton = itemView.findViewById(R.id.item_service_delete_button);
            saveButton = itemView.findViewById(R.id.item_service_save_button);
        }

        public void bindService(final Service service){
            this.service = service;
            nameEditText.setText(service.getName());
            priceEditText.setText(String.valueOf(service.getPrice()));
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    serviceList.remove(service);
                    serviceHolderAdapter.notifyDataSetChanged();
                    deleteService(service);
                }
            });
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    service.setName(nameEditText.getText().toString());
                    service.setPrice(Double.valueOf(priceEditText.getText().toString()));
                    saveService(service);
                }
            });
        }

        public Service getService() {
            return service;
        }
    }

    private void saveService(Service service){
        App.getApi().refreshService(service).enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {
                Toast.makeText(getContext(), "not saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteService(Service service){
        App.getApi().deleteService(service).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Not deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
