package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity

public class PetModel {
    @PrimaryKey
    public Integer PetId;
    public String PetSpecies;
    public String PetBreed;
    public String Name;
    public String ClientDesc;
    public Double Height;
    public Double Length;
    public String ClientUserName;

    public PetModel() {
    }

    public Integer getPetId() {
        return PetId;
    }

    public void setPetId(Integer petId) {
        PetId = petId;
    }

    public String getPetSpecies() {
        return PetSpecies;
    }

    public void setPetSpecies(String petSpecies) {
        PetSpecies = petSpecies;
    }

    public String getPetBreed() {
        return PetBreed;
    }

    public void setPetBreed(String petBreed) {
        PetBreed = petBreed;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getClientDesc() {
        return ClientDesc;
    }

    public void setClientDesc(String clientDesc) {
        ClientDesc = clientDesc;
    }

    public Double getHeight() {
        return Height;
    }

    public void setHeight(Double height) {
        Height = height;
    }

    public Double getLength() {
        return Length;
    }

    public void setLength(Double length) {
        Length = length;
    }

    public String getClientUserName() {
        return ClientUserName;
    }

    public void setClientUserName(String clientUserName) {
        ClientUserName = clientUserName;
    }
}
