package com.example.asztar.wetklinikmobileapp.Models;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

@Database(entities = {AddressModel.class, AppointmentModel.class, AppoitmentPetModel.class, ClientModel.class, ClinicModel.class, EmployeeModel.class, PetModel.class, PetTreatmentModel.class, PhoneNumberModel.class, TreatmentModel.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class ClinicDb extends RoomDatabase {
    private static ClinicDb INSTANCE;
    private static final String DB_NAME = "Clinic.db";
    //public ClinicDb(){}
    public static ClinicDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ClinicDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ClinicDb.class, DB_NAME)
                            .allowMainThreadQueries() // SHOULD NOT BE USED IN PRODUCTION !!!
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Log.d("ClinicDb", "populating with data...");
                                    //new PopulateDbAsync(INSTANCE).execute();
                                }
                            })
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public void clearDb() {
        if (INSTANCE != null) {
            //new PopulateDbAsync(INSTANCE).execute();
        }
    }

    /*public abstract ClinicModel ClinicDao();
    public abstract AppointmentModel AppointmentDao();
    public abstract EmployeeModel EmployeeDao();
    public abstract PetModel PetDao();
    public abstract PetTreatmentModel PetTreatmentDao();*/

    /*private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final MovieDao movieDao;
        private final DirectorDao directorDao;

        public PopulateDbAsync(ClinicDb instance) {
            movieDao = instance.movieDao();
            directorDao = instance.directorDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            movieDao.deleteAll();
            directorDao.deleteAll();

            Director directorOne = new Director("Adam McKay");
            Director directorTwo = new Director("Denis Villeneuve");
            Director directorThree = new Director("Morten Tyldum");

            Movie movieOne = new Movie("The Big Short", (int) directorDao.insert(directorOne));
            final int dIdTwo = (int) directorDao.insert(directorTwo);
            Movie movieTwo = new Movie("Arrival", dIdTwo);
            Movie movieThree = new Movie("Blade Runner 2049", dIdTwo);
            Movie movieFour = new Movie("Passengers", (int) directorDao.insert(directorThree));

            movieDao.insert(movieOne, movieTwo, movieThree, movieFour);

            return null;
        }*/
}