package com.example.asztar.wetklinikmobileapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asztar.wetklinikmobileapp.Adapters.PetTreatmentRecyclerAdapter;
import com.example.asztar.wetklinikmobileapp.ApiConn.ApiConnection;
import com.example.asztar.wetklinikmobileapp.ApiConn.Controller;
import com.example.asztar.wetklinikmobileapp.ApiConn.RestResponse;
import com.example.asztar.wetklinikmobileapp.ApiConn.Settings;
import com.example.asztar.wetklinikmobileapp.Connectivity;
import com.example.asztar.wetklinikmobileapp.MenuBase;
import com.example.asztar.wetklinikmobileapp.Models.ClinicDb;
import com.example.asztar.wetklinikmobileapp.Models.Converters;
import com.example.asztar.wetklinikmobileapp.Models.PetDao;
import com.example.asztar.wetklinikmobileapp.Models.PetModel;
import com.example.asztar.wetklinikmobileapp.Models.PetTreatmentDao;
import com.example.asztar.wetklinikmobileapp.Models.PetTreatmentModel;
import com.example.asztar.wetklinikmobileapp.R;
import com.example.asztar.wetklinikmobileapp.Token;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.Arrays;

public class PetTreatmentActivity extends MenuBase {

    public static final String ARG_ITEM_ID = "item_id";

    Token token = Token.getInstance();
    private PetTreatmentDao petTreatmentDao;
    private PetDao petDao;
    private PetModel pet;
    private ArrayList<PetTreatmentModel> petTreatmentArrayList;
    private SharedPreferences preferences;
    private DateTime upDate;
    private Boolean forceRedownload = false;
    private DownloadPetTreatment downloadPetTreatment;

    private TextView tvPetName;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_treatment);
        recyclerView = findViewById(R.id.rvPetTreatment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        assert recyclerView != null;
        tvPetName = findViewById(R.id.tvPetTreatmentPetName);
        petDao = ClinicDb.getDatabase(PetTreatmentActivity.this).PetDao();
        Bundle b = getIntent().getExtras();
        if (b.containsKey(ARG_ITEM_ID)) {
            pet = petDao.findPetById(Integer.valueOf(b.getString(ARG_ITEM_ID)));
            tvPetName.setText(pet.getPetName());
            preferences = this.getSharedPreferences(token.getUserName(), Context.MODE_PRIVATE);
            long longDate = preferences.getLong("updatePetTreatmentId" + b.getString(ARG_ITEM_ID) + "Time", 0);
            upDate = Converters.fromTimestamp(longDate);
        }
        fab = findViewById(R.id.fabRefreshPetTreatmentList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.connectionIsUp(PetTreatmentActivity.this)) {
                    forceRedownload = true;
                    downloadPetTreatment = new DownloadPetTreatment();
                    downloadPetTreatment.execute();
                } else {
                    Toast.makeText(PetTreatmentActivity.this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (token.getAccess_token() == null)
            logout();
        downloadPetTreatment = new DownloadPetTreatment();
        downloadPetTreatment.execute();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new PetTreatmentRecyclerAdapter(petTreatmentArrayList));
    }

    public class DownloadPetTreatment extends AsyncTask<Void, Void, Integer> {

        DownloadPetTreatment() {
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int code = -1;
            if (Days.daysBetween(upDate, DateTime.now()).getDays() > 3 && Connectivity.connectionIsUp(PetTreatmentActivity.this) || forceRedownload && Connectivity.connectionIsUp(PetTreatmentActivity.this)) {
                try {
                    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    Controller controller = new Controller(new ApiConnection(Settings.BASE_URL));
                    mapper.registerModule(new JodaModule());
                    RestResponse response = controller.getPetTreatment(pet.getPetId().toString());
                    PetTreatmentModel[] petTreatmentModels = mapper.readValue(response.getResponse(), PetTreatmentModel[].class);
                    petTreatmentArrayList = new ArrayList<PetTreatmentModel>(Arrays.asList(petTreatmentModels));
                    code = response.getResponseCode();
                    if (code == 200) {
                        PetTreatmentDao petTreatmentDao = ClinicDb.getDatabase(PetTreatmentActivity.this).PetTreatmentDao();
                        petTreatmentDao.insert(petTreatmentArrayList.toArray(new PetTreatmentModel[petTreatmentArrayList.size()]));
                        preferences.edit().putLong("updatePetTreatmentId" + pet.getPetId() + "Time", Converters.dateToTimestamp(DateTime.now())).apply();
                    }
                    forceRedownload = false;
                    return code;
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            else if(upDate != Converters.fromTimestamp(Long.valueOf(0))){
                petTreatmentDao = ClinicDb.getDatabase(PetTreatmentActivity.this).PetTreatmentDao();
                petTreatmentArrayList = new ArrayList<PetTreatmentModel>(petTreatmentDao.getAllPetTreatmentOfPet(pet.getPetId()));
                code = 200;
            }
            return code;
        }

        @Override
        protected void onPostExecute(final Integer success) {

            if (success == 200) {
                setupRecyclerView(recyclerView);
            } else {
                Toast.makeText(PetTreatmentActivity.this, "Nie udało się pobrać karty zdrowia", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
