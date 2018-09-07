package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.RoomWarnings;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "pettreatment")
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class PetTreatmentModel {
    @PrimaryKey
    @JsonProperty("PetTreatmentId")
    private Integer PetTreatmentId;
    @JsonProperty("Date")
    private DateTime Date;
    @JsonProperty("PetId")
    private String PetId;
    @Embedded
    @JsonProperty("Treatment")
    private TreatmentModel Treatment;
    @JsonProperty("PetTreatmentDesc")
    private String PetTreatmentDesc;
    @Embedded
    @JsonProperty("EmployeePetTreatment")
    private EmployeeModel EmployeePetTreatment;
    @JsonProperty("ClientUserName")
    private String ClientUserName;

    public PetTreatmentModel() {
    }

    public String getPetId() {
        return PetId;
    }

    public void setPetId(String petId) {
        PetId = petId;
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

    public String getPetTreatmentDesc() {
        return PetTreatmentDesc;
    }

    public void setPetTreatmentDesc(String desc) {
        PetTreatmentDesc = desc;
    }

    public EmployeeModel getEmployeePetTreatment() {
        return EmployeePetTreatment;
    }

    public void setEmployeePetTreatment(EmployeeModel employeePetTreatment) {
        EmployeePetTreatment = employeePetTreatment;
    }

    public String getClientUserName() {
        return ClientUserName;
    }

    public void setClientUserName(String clientUserName) {
        ClientUserName = clientUserName;
    }
}


