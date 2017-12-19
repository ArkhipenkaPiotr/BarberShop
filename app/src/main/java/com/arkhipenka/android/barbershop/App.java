package com.arkhipenka.android.barbershop;

import android.app.Application;
import android.text.format.DateFormat;

import com.arkhipenka.android.barbershop.Entities.Accessable;
import com.arkhipenka.android.barbershop.Entities.Hairdresser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by arkhipenka_piotr on 09.11.2017.
 */

public class App extends Application {

    private Retrofit retrofit;
    private static BarberApi barberApi = null;
    private static Accessable accessable;

    @Override
    public void onCreate() {
        super.onCreate();
        accessable = null;

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://d755e098.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        barberApi = retrofit.create(BarberApi.class);
    }

    public static BarberApi getApi(){
        return barberApi;
    }

    public static Accessable getAccessable() {
        return accessable;
    }

    public static void setAccessable(Accessable accessable) {
        App.accessable = accessable;
    }
}
