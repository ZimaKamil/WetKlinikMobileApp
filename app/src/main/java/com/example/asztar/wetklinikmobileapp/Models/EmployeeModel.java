package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "employee")
public class EmployeeModel {
    @PrimaryKey
    public Integer EmployeeId;
    public Integer ClinicId;
    public Boolean ShowInMobileApp;
    public String Name;
    public String Surname;
    public String Position;
    public String Desc;

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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }
}