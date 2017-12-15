package com.arkhipenka.android.barbershop.Entities;

public interface Accessable {
    public final static String TYPE_USER = "user";
    public final static String TYPE_ADMIN = "admin";
    public final static String TYPE_HAIRDRESSER = "hairdresser";

    String getPermissions();
    long getId();
}
