package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ClinicDao {
    @Query("SELECT * FROM clinic WHERE ClinicId = :id LIMIT 1")
    ClinicModel findClinicById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ClinicModel... clinicModels);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ClinicModel clinicModels);

    @Query("DELETE FROM clinic")
    void deleteAll();
}
