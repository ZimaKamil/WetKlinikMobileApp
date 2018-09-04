package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.TypeConverter;

import java.time.LocalDateTime;
import org.joda.time.DateTime;
@Entity
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class AppointmentModel
{
    @PrimaryKey
    public Integer appointmentId;
    public DateTime date;
    @Embedded
    public EmployeeModel employee;
    public String status;
    public DateTime statusChangeDate;
    public String clientUserName;

    public AppointmentModel() {
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public EmployeeModel getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeModel employee) {
        this.employee = employee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getStatusChangeDate() {
        return statusChangeDate;
    }

    public void setStatusChangeDate(DateTime statusChangeDate) {
        this.statusChangeDate = statusChangeDate;
    }

    public String getClientUserName() {
        return clientUserName;
    }

    public void setClientUserName(String clientUserName) {
        this.clientUserName = clientUserName;
    }
}

