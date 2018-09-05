package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity

public class PhoneNumberModel {
    @PrimaryKey
    public Integer PhoneNumberId;
    public String Number;

    public PhoneNumberModel() {
    }

    public Integer getPhoneNumberId() {
        return PhoneNumberId;
    }

    public void setPhoneNumberId(Integer phoneNumberId) {
        PhoneNumberId = phoneNumberId;
    }

    public String getPhoneNumber() {
        return Number;
    }

    public void setPhoneNumber(String phoneNumber) {
        Number = phoneNumber;
    }
}
