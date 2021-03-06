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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

public class ClinicModel {
    @JsonProperty("ClinicId")
    private Integer ClinicId;
    @JsonProperty("Address")
    private AddressModel Address;
    @JsonProperty("ClinicName")
    private String ClinicName;
    @JsonProperty("OpeningHours")
    private String OpeningHours;
    @JsonProperty("ClinicPhoneNumber")
    private ArrayList<PhoneNumberModel> ClinicPhoneNumber;

    public ClinicModel() {
    }

    public ClinicModel(ClinicRoomModel model, ArrayList<PhoneNumberModel> numberModel) {
        ClinicId = model.getClinicId();
        Address = model.getAddress();
        ClinicName = model.getName();
        OpeningHours = model.getOpeningHours();
        ClinicPhoneNumber = numberModel;
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

    public String getClinicName() {
        return ClinicName;
    }

    public void setClinicName(String clinicName) {
        ClinicName = clinicName;
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
