package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(tableName = "phonenumber")

public class PhoneNumberModel {
    @PrimaryKey
    @JsonProperty("PhoneNumberId")
    private Integer PhoneNumberId;
    @JsonProperty("ClinicId")
    private Integer ClinicId;
    @JsonProperty("Number")
    private String Number;

    public PhoneNumberModel() {
    }

    public Integer getClinicId() {
        return ClinicId;
    }

    public void setClinicId(Integer clinicId) {
        ClinicId = clinicId;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public Integer getPhoneNumberId() {
        return PhoneNumberId;
    }

    public void setPhoneNumberId(Integer phoneNumberId) {
        PhoneNumberId = phoneNumberId;
    }
}
