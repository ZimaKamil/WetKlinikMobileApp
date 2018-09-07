package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface AppointmentDao {
    @Query("SELECT * FROM appointment WHERE AppointmentId = :id LIMIT 1")
    AppointmentModel findAppointmentById(int id);

    @Query("SELECT * FROM appointment WHERE ClientUserName = :username")
    List<AppointmentModel> findAppointmentByUser(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AppointmentModel... appointmentModel);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(AppointmentModel appointmentModel);

    @Query("DELETE FROM appointment")
    void deleteAll();

    @Query("SELECT * FROM appointment ")
    List<AppointmentModel> getAllAppointment();
}