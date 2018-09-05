package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity

public class TreatmentModel {
    @PrimaryKey
    public Integer TreatmentId;
    public String Name;

    public TreatmentModel() {
    }

    public Integer getTreatmentId() {
        return TreatmentId;
    }

    public void setTreatmentId(Integer treatmentId) {
        TreatmentId = treatmentId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
