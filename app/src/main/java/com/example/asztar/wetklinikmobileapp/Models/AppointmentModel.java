package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.TypeConverter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

import org.joda.time.DateTime;

@Entity(tableName = "appointment")
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class AppointmentModel {
    @PrimaryKey
    @JsonProperty("AppointmentId")
    private Integer AppointmentId;
    @JsonProperty("Date")
    private DateTime Date;
    @Embedded
    @JsonProperty("Employee")
    private EmployeeModel Employee;
    @JsonProperty("Status")
    private int Status;
    @JsonProperty("StatusChangeDate")
    private DateTime StatusChangeDate;
    @JsonProperty("ClientUserName")
    private String ClientUserName;

    public AppointmentModel() {
    }

    public Integer getAppointmentId() {
        return AppointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        AppointmentId = appointmentId;
    }

    public DateTime getDate() {
        return Date;
    }

    public void setDate(DateTime date) {
        Date = date;
    }

    public EmployeeModel getEmployee() {
        return Employee;
    }

    public void setEmployee(EmployeeModel employee) {
        Employee = employee;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public DateTime getStatusChangeDate() {
        return StatusChangeDate;
    }

    public void setStatusChangeDate(DateTime statusChangeDate) {
        StatusChangeDate = statusChangeDate;
    }

    public String getClientUserName() {
        return ClientUserName;
    }

    public void setClientUserName(String clientUserName) {
        ClientUserName = clientUserName;
    }
}

