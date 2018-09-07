package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.RoomWarnings;
import android.support.annotation.Nullable;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "clinic")
public class ClinicRoomModel {
    @PrimaryKey
    private Integer ClinicId;
    @Embedded
    private AddressModel Address;
    private String Name;
    private String OpeningHours;

    public ClinicRoomModel() {
    }

    @Ignore
    public ClinicRoomModel(ClinicModel model) {
        ClinicId = model.getClinicId();
        Address = model.getAddress();
        Name = model.getClinicName();
        OpeningHours = model.getOpeningHours();
    }

    public Integer getClinicId() {
        return ClinicId;
    }

    public void setClinicId(Integer clinicId) {
        ClinicId = clinicId;
    }

    public AddressModel getAddress() {
        return Address;
    }

    public void setAddress(AddressModel address) {
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOpeningHours() {
        return OpeningHours;
    }

    public void setOpeningHours(String openingHours) {
        OpeningHours = openingHours;
    }
}
