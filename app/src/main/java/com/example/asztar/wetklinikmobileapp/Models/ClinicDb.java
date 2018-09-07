package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

@Database(entities = {AppointmentModel.class, ClinicRoomModel.class, EmployeeModel.class, PetModel.class, PetTreatmentModel.class, PhoneNumberModel.class}, version = 7)
@TypeConverters({Converters.class})
public abstract class ClinicDb extends RoomDatabase {
    private static ClinicDb INSTANCE;
    private static final String DB_NAME = "Clinic.db";

    public ClinicDb(){}
    public static ClinicDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ClinicDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ClinicDb.class, DB_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                }
                            })
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract EmployeeDao EmployeeDao();

    public abstract PetDao PetDao();

    public abstract ClinicDao ClinicDao();

    public abstract PhoneNumberDao PhoneNumberDao();

    public abstract AppointmentDao AppointmentDao();

    public abstract PetTreatmentDao PetTreatmentDao();
}