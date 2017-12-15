package com.arkhipenka.android.barbershop.Entities;

import java.io.Serializable;
import java.util.Date;

public class Entry implements Serializable {

    private long id;
    private Hairdresser hairdresser;
    private BarberUser barberUser;
    private Date serviceTime;
    private Service service;

    public Entry(Hairdresser hairdresser, BarberUser barberUser, Date serviceTime, Service service) {
        this.hairdresser = hairdresser;
        this.barberUser = barberUser;
        this.serviceTime = serviceTime;
        this.service = service;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Hairdresser getHairdresser() {
        return hairdresser;
    }

    public void setHairdresser(Hairdresser hairdresser) {
        this.hairdresser = hairdresser;
    }

    public BarberUser getBarberUser() {
        return barberUser;
    }

    public void setBarberUser(BarberUser barberUser) {
        this.barberUser = barberUser;
    }

    public Date getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Date serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
