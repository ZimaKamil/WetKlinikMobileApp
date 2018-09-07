package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity

public class AppoitmentPetModel {
    @PrimaryKey
    private Integer PetId;
    private String Name;

    public AppoitmentPetModel() {
    }

    public Integer getPetId() {
        return PetId;
    }

    public void setPetId(Integer petId) {
        PetId = petId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
