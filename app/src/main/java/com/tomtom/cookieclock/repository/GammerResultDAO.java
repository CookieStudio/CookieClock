package com.tomtom.cookieclock.repository;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

/**
 * Created by saramakm on 14-10-15.
 */
@DatabaseTable(tableName = "gammer")
public class GammerResultDAO {


    @DatabaseField(id = true)
    private String uuid;


    @DatabaseField
    private String name;


    @DatabaseField
    private String email;

    @DatabaseField(columnName = "timeinms")
    private long timeinms;

    /**
     * Dont use only for ORM
     *
     */
    @Deprecated
    public GammerResultDAO(){
    }

    public static GammerResultDAO createGammerResult(String name, String surname, long timeinms){
        GammerResultDAO res =   new GammerResultDAO();
        res.setUuid(UUID.randomUUID().toString());
        res.name = name;
        res.email = surname;
        res.timeinms = timeinms;
        return res;
    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTimeinMs() {
        return timeinms;
    }

    public void setTimeinMs(long timeinMs) {
        this.timeinms = timeinMs;
    }
}
