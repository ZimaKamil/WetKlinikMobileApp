package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.joda.time.DateTime;

@Entity(tableName = "pet")

public class PetModel {
    @PrimaryKey
    @JsonProperty("PetId")
    private Integer PetId;
    @JsonProperty("PetSpecies")
    private String PetSpecies;
    @JsonProperty("PetBreed")
    private String PetBreed;
    @JsonProperty("PetName")
    private String PetName;
    @JsonProperty("ClientDesc")
    private String ClientDesc;
    @JsonProperty("Height")
    private Double Height;
    @JsonProperty("Length")
    private Double Length;
    @JsonProperty("Sex")
    private Integer Sex;
    @JsonProperty("BirthDate")
    private DateTime BirthDate;
    @JsonProperty("ClientUserName")
    private String ClientUserName;

    public PetModel() {
    }

    public Integer getSex() {
        return Sex;
    }

    public void setSex(Integer sex) {
        Sex = sex;
    }

    public DateTime getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(DateTime birthDate) {
        BirthDate = birthDate;
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

    public String getPetName() {
        return PetName;
    }

    public void setPetName(String petName) {
        PetName = petName;
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
