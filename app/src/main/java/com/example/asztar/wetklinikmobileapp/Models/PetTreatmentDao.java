package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.asztar.wetklinikmobileapp.Models.PetModel;

import java.util.List;

@Dao
public interface PetTreatmentDao {
    @Query("SELECT * FROM pettreatment WHERE PetTreatmentId = :id LIMIT 1")
    PetTreatmentModel findPetTreatmentById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PetTreatmentModel... petTreatmentModels);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(PetTreatmentModel petTreatmentModel);

    @Query("DELETE FROM pettreatment")
    void deleteAll();

    @Query("SELECT * FROM pettreatment ")
    List<PetTreatmentModel> getAllPetTreatments();

    @Query("SELECT * FROM pettreatment WHERE PetId = :petId ")
    List<PetTreatmentModel> getAllPetTreatmentOfPet(int petId);
}
