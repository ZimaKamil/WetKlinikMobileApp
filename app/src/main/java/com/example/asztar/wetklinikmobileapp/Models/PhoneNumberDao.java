package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
@Dao
public interface PhoneNumberDao {
    @Query("SELECT * FROM phonenumber WHERE ClinicId = :id LIMIT 1")
    PhoneNumberModel findNumberById(int id);

    @Query("SELECT * FROM phonenumber WHERE ClinicId = :clinicId")
    List<PhoneNumberModel> findNumberByClinic(int clinicId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PhoneNumberModel... phoneNumberModels);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(PhoneNumberModel phoneNumberModels);

    @Query("DELETE FROM phonenumber")
    void deleteAll();

    @Query("SELECT * FROM phonenumber ")
    List<PhoneNumberModel> getAllPhoneNumber();
}
