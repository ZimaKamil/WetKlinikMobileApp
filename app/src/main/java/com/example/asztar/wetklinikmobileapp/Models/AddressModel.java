package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(tableName = "address")
public class AddressModel {
    @PrimaryKey
    @JsonProperty("AddressId")
    private Integer AddressId;
    @JsonProperty("Town")
    private String Town;
    @JsonProperty("Street")
    private String Street;
    @JsonProperty("BuildingNr")
    private String BuildingNr;
    @JsonProperty("PostalCode")
    private String PostalCode;
    @JsonProperty("AddressType")
    private String AddressType;

    public AddressModel() {
    }


    public Integer getAddressId() {
        return AddressId;
    }

    public void setAddressId(Integer addressId) {
        AddressId = addressId;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getBuildingNr() {
        return BuildingNr;
    }

    public void setBuildingNr(String buildingNr) {
        BuildingNr = buildingNr;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getAddressType() {
        return AddressType;
    }

    public void setAddressType(String addressType) {
        AddressType = addressType;
    }
}