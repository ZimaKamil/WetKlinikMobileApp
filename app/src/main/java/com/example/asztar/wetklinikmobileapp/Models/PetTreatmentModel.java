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
    public Integer petTreatmentId;
    public DateTime date;
    @Embedded
    public TreatmentModel treatment;
    public String desc;
    @Embedded
    public ArrayList<EmployeeModel> employeePetTreatment;
    public String clientUserName;

    public PetTreatmentModel() {
    }

    public Integer getPetTreatmentId() {
        return petTreatmentId;
    }

    public void setPetTreatmentId(Integer petTreatmentId) {
        this.petTreatmentId = petTreatmentId;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public TreatmentModel getTreatment() {
        return treatment;
    }

    public void setTreatment(TreatmentModel treatment) {
        this.treatment = treatment;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<EmployeeModel> getEmployeePetTreatment() {
        return employeePetTreatment;
    }

    public void setEmployeePetTreatment(ArrayList<EmployeeModel> employeePetTreatment) {
        this.employeePetTreatment = employeePetTreatment;
    }

    public String getClientUserName() {
        return clientUserName;
    }

    public void setClientUserName(String clientUserName) {
        this.clientUserName = clientUserName;
    }
}


