package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.RoomWarnings;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
@Entity
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class PetTreatmentModel {
    @PrimaryKey
    public Integer PetTreatmentId;
    public DateTime Date;
    @Embedded
    public TreatmentModel Treatment;
    public String Desc;
    @Embedded
    public ArrayList<EmployeeModel> EmployeePetTreatment;
    public String ClientUserName;

    public PetTreatmentModel() {
    }

    public Integer getPetTreatmentId() {
        return PetTreatmentId;
    }

    public void setPetTreatmentId(Integer petTreatmentId) {
        PetTreatmentId = petTreatmentId;
    }

    public DateTime getDate() {
        return Date;
    }

    public void setDate(DateTime date) {
        Date = date;
    }

    public TreatmentModel getTreatment() {
        return Treatment;
    }

    public void setTreatment(TreatmentModel treatment) {
        Treatment = treatment;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public ArrayList<EmployeeModel> getEmployeePetTreatment() {
        return EmployeePetTreatment;
    }

    public void setEmployeePetTreatment(ArrayList<EmployeeModel> employeePetTreatment) {
        EmployeePetTreatment = employeePetTreatment;
    }

    public String getClientUserName() {
        return ClientUserName;
    }

    public void setClientUserName(String clientUserName) {
        ClientUserName = clientUserName;
    }
}


