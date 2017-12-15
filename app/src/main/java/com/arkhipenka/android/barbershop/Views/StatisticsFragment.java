package com.arkhipenka.android.barbershop.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arkhipenka.android.barbershop.App;
import com.arkhipenka.android.barbershop.Entities.Entry;
import com.arkhipenka.android.barbershop.Entities.Hairdresser;
import com.arkhipenka.android.barbershop.Entities.WorkingHours;
import com.arkhipenka.android.barbershop.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

/**
 * Created by arkhipenka_piotr on 08.12.2017.
 */

public class StatisticsFragment extends Fragment {
    List<Hairdresser> hairdressers;
    List<Entry> entries;
    PieChart pieChart1;
    PieChart pieChart2;
    List<PieEntry> chartEntries;
    PieDataSet pieDataSet;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistics,container,false);
        pieChart1 = v.findViewById(R.id.fragment_statistics_pie_char_1);
        pieChart2 = v.findViewById(R.id.fragment_statistics_pie_char_2);
        progressBar = v.findViewById(R.id.fragment_statistics_tool_bar);
        fillHairdressers();
        return v;
    }

    private float calculateHairdresserEntriesOnThisMonth(Hairdresser hairdresser){
        float amount = 0;
        DateTimeZone zone = DateTimeZone.forID( "America/Montreal" );
        DateTime now = DateTime.now( zone );
        for (com.arkhipenka.android.barbershop.Entities.Entry entry:entries){
            DateTime dateTime = new DateTime(entry.getServiceTime() , zone );
            if (entry.getHairdresser().getId()==hairdresser.getId()&&dateTime.getMonthOfYear()==now.getMonthOfYear()&&dateTime.getYear()==now.getYear()){
                amount++;
            }
        }
        return amount;
    }

    private float calculateHairdresserEntriesCashOnThisMonth(Hairdresser hairdresser){
        float cash = 0;
        DateTimeZone zone = DateTimeZone.forID( "America/Montreal" );
        DateTime now = DateTime.now( zone );
        for (com.arkhipenka.android.barbershop.Entities.Entry entry:entries){
            DateTime dateTime = new DateTime(entry.getServiceTime() , zone );
            if (entry.getHairdresser().getId()==hairdresser.getId()&&dateTime.getMonthOfYear()==now.getMonthOfYear()&&dateTime.getYear()==now.getYear()){
                cash+=entry.getService().getPrice();
            }
        }
        return cash;
    }

    private void initChart1(){
        chartEntries = new ArrayList<>();
        for (Hairdresser hairdresser:hairdressers){
            chartEntries.add(new PieEntry(calculateHairdresserEntriesOnThisMonth(hairdresser),hairdresser.getLastName()));
        }
        pieDataSet = new PieDataSet(chartEntries,"Label1");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);

        pieChart1.setData(pieData);
        pieChart1.setDescription(new Description());
    }

    private void initChart2(){
        chartEntries = new ArrayList<>();
        for (Hairdresser hairdresser:hairdressers){
            chartEntries.add(new PieEntry(calculateHairdresserEntriesCashOnThisMonth(hairdresser),hairdresser.getLastName()));
        }
        pieDataSet = new PieDataSet(chartEntries,null);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);

        pieChart2.setData(pieData);
        pieChart2.setDescription(new Description());
    }

    private void fillHairdressers(){
        App.getApi().getHairdressers().enqueue(new Callback<List<Hairdresser>>() {
            @Override
            public void onResponse(Call<List<Hairdresser>> call, Response<List<Hairdresser>> response) {
                hairdressers = response.body();
                fillEntries();
            }

            @Override
            public void onFailure(Call<List<Hairdresser>> call, Throwable t) {
                progressBar.setVisibility(GONE);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillEntries(){
        App.getApi().getEntries().enqueue(new Callback<List<Entry>>() {
            @Override
            public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                entries = response.body();
                progressBar.setVisibility(GONE);
                initChart1();
                initChart2();
            }

            @Override
            public void onFailure(Call<List<Entry>> call, Throwable t) {
                Toast.makeText(getContext(),"Fail",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(GONE);
            }
        });
    }
}
