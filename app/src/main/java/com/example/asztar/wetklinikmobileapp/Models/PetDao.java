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
public interface PetDao {
    @Query("SELECT * FROM pet WHERE PetId = :id LIMIT 1")
    PetModel findPetById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PetModel... petModels);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(PetModel petModel);

    @Query("DELETE FROM pet")
    void deleteAll();

    @Query("SELECT * FROM pet ")
    List<PetModel> getAllPets();

    @Query("SELECT * FROM pet WHERE ClientUserName = :username ")
    List<PetModel> getAllPetsOfUser(String username);
}
