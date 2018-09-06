package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.RoomWarnings;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "clinic")
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class ClinicModel {
    @PrimaryKey
    public Integer ClinicId;
    @Embedded
    public AddressModel Address;
    public String Name;
    public String OpeningHours;
    @Embedded
    public ArrayList<PhoneNumberModel> ClinicPhoneNumber;

    public ClinicModel() {
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

    public ArrayList<PhoneNumberModel> getClinicPhoneNumber() {
        return ClinicPhoneNumber;
    }

    public void setClinicPhoneNumber(ArrayList<PhoneNumberModel> clinicPhoneNumber) {
        ClinicPhoneNumber = clinicPhoneNumber;
    }
}
