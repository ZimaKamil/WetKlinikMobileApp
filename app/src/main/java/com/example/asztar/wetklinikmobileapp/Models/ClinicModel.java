package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.RoomWarnings;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
@Entity
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class ClinicModel {
    @PrimaryKey
    public Integer clinicId;
    @Embedded
    public AddressModel address;
    public String name;
    public String openingHours;
    @Embedded
    public ArrayList<PhoneNumberModel> clinicPhoneNumber;

    public ClinicModel() {
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public List<PhoneNumberModel> getClinicPhoneNumber() {
        return clinicPhoneNumber;
    }

    public void setClinicPhoneNumber(ArrayList<PhoneNumberModel> clinicPhoneNumber) {
        this.clinicPhoneNumber = clinicPhoneNumber;
    }
}
