package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface EmployeeDao {
    @Query("SELECT * FROM employee WHERE EmployeeId = :id LIMIT 1")
    EmployeeModel findEmployeeById(int id);

    @Query("SELECT * FROM employee WHERE ClinicId = :clinicId")
    List<EmployeeModel> findEmployeeByClinic(int clinicId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EmployeeModel... employeeModels);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(EmployeeModel employeeModels);

    @Query("DELETE FROM employee")
    void deleteAll();

    @Query("SELECT * FROM employee ")
    List<EmployeeModel> getAllEmployee();
}
