package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(tableName = "employee")
public class EmployeeModel {
    @PrimaryKey
    @JsonProperty("EmployeeId")
    private Integer EmployeeId;
    @JsonProperty("ClinicId")
    private Integer ClinicId;
    @JsonProperty("ShowInMobileApp")
    private Boolean ShowInMobileApp;
    @JsonProperty("EmployeeName")
    private String EmployeeName;
    @JsonProperty("Surname")
    private String Surname;
    @JsonProperty("Position")
    private String Position;
    @JsonProperty("EmployeeDesc")
    private String EmployeeDesc;

    public EmployeeModel() {
    }

    public Integer getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        EmployeeId = employeeId;
    }

    public Integer getClinicId() {
        return ClinicId;
    }

    public void setClinicId(Integer clinicId) {
        ClinicId = clinicId;
    }

    public Boolean getShowInMobileApp() {
        return ShowInMobileApp;
    }

    public void setShowInMobileApp(Boolean showInMobileApp) {
        ShowInMobileApp = showInMobileApp;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getEmployeeDesc() {
        return EmployeeDesc;
    }

    public void setEmployeeDesc(String employeeDesc) {
        EmployeeDesc = employeeDesc;
    }
}