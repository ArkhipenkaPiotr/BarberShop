package com.arkhipenka.android.barbershop;

import com.arkhipenka.android.barbershop.Entities.BarberUser;
import com.arkhipenka.android.barbershop.Entities.Entry;
import com.arkhipenka.android.barbershop.Entities.Hairdresser;
import com.arkhipenka.android.barbershop.Entities.Service;
import com.arkhipenka.android.barbershop.Entities.WorkingHours;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by arkhipenka_piotr on 09.11.2017.
 */

public interface BarberApi {
    @GET("/hairdresser")
    Call<List<Hairdresser>> getHairdressers();

    @GET("/barber_user")
    Call<BarberUser> getBarberUser(@Query("login") String login, @Query("password") String password);

    @GET("/hairdresser/{id}/working_hours")
    Call<WorkingHours> getWorkingHoursOf(@Path("id") Long hairdresserId);

    @GET("/hairdresser/{id}/entries")
    Call<List<Entry>> getHairdresserEntriesOf(@Path("id") Long hairdresserId);

    @GET("/hairdresser/{id}")
    Call<Hairdresser> getHairdresser(@Path("id") Long hairdresserId);

    @GET("/hairdresser")
    Call<Hairdresser> getHairdresser(@Query("lastname") String lastname, @Query("password") String password);

    @POST("/working_hours")
    Call<WorkingHours> addWorkingHours(@Body WorkingHours workingHours);

    @PUT("/hairdresser")
    Call<Hairdresser> addHairdresser(@Body Hairdresser hairdresser);

    @PUT("/barber_user")
    Call<BarberUser> saveUser(@Body BarberUser barberUser);

    @GET("/barber_user")
    Call<List<BarberUser>> getUsers();

    @DELETE("/hairdresser/{id}")
    Call<String> deleteHairdresser(@Path("id") Long id);

    @GET("/barber_user/{id}/entries")
    Call<List<Entry>> getEntriesOf(@Path("id") long userId);

    @GET("/service")
    Call<List<Service>> getServices();

    @POST("/service")
    Call<Service> refreshService(@Body Service service);

    @DELETE("/service")
    Call<String> deleteService(@Body Service service);

    @GET ("/entry")
    Call<List<Entry>> getEntries();

    @POST("/barber_user/registration")
    Call<BarberUser> userRegistration(@Query("login") String login, @Query("password") String password);

    @GET("/barber_user/{id}")
    Call<BarberUser> getUser(@Path("id")long userId);

    @POST("/entry")
    Call<Entry> addEntry(@Body Entry entry);
}
