package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity

public class TreatmentModel {
    @PrimaryKey
    @JsonProperty("TreatmentId")
    private Integer TreatmentId;
    @JsonProperty("TreatmentName")
    private String TreatmentName;

    public TreatmentModel() {
    }

    public Integer getTreatmentId() {
        return TreatmentId;
    }

    public void setTreatmentId(Integer treatmentId) {
        TreatmentId = treatmentId;
    }

    public String getTreatmentName() {
        return TreatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        TreatmentName = treatmentName;
    }
}
