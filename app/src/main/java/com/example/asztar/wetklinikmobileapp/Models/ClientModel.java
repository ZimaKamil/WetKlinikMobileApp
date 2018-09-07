package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity

public class ClientModel {
    @PrimaryKey
    @JsonProperty("PersonId")
    private Integer PersonId;
    @JsonProperty("ClinicId")
    private Integer ClinicId;
    @JsonProperty("Email")
    private String Email;
    @JsonProperty("ClientName")
    private String ClientName;
    @JsonProperty("ClientSurname")
    private String ClientSurname;

    public ClientModel() {
    }

    public Integer getPersonId() {
        return PersonId;
    }

    public void setPersonId(Integer personId) {
        PersonId = personId;
    }

    public Integer getClinicId() {
        return ClinicId;
    }

    public void setClinicId(Integer clinicId) {
        ClinicId = clinicId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientSurname() {
        return ClientSurname;
    }

    public void setClientSurname(String clientSurname) {
        ClientSurname = clientSurname;
    }
}
