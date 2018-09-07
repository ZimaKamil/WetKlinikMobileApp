package com.example.asztar.wetklinikmobileapp.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asztar.wetklinikmobileapp.Adapters.ClinicPhoneNumberRecyclerAdapter;
import com.example.asztar.wetklinikmobileapp.ApiConn.ApiConnection;
import com.example.asztar.wetklinikmobileapp.ApiConn.RestResponse;
import com.example.asztar.wetklinikmobileapp.ApiConn.Controller;
import com.example.asztar.wetklinikmobileapp.Connectivity;
import com.example.asztar.wetklinikmobileapp.MenuBase;
import com.example.asztar.wetklinikmobileapp.Models.ClinicDao;
import com.example.asztar.wetklinikmobileapp.Models.ClinicDb;
import com.example.asztar.wetklinikmobileapp.Models.ClinicModel;
import com.example.asztar.wetklinikmobileapp.Models.ClinicRoomModel;
import com.example.asztar.wetklinikmobileapp.Models.Converters;
import com.example.asztar.wetklinikmobileapp.Models.PetModel;
import com.example.asztar.wetklinikmobileapp.Models.PhoneNumberDao;
import com.example.asztar.wetklinikmobileapp.Models.PhoneNumberModel;
import com.example.asztar.wetklinikmobileapp.R;
import com.example.asztar.wetklinikmobileapp.ApiConn.Settings;
import com.example.asztar.wetklinikmobileapp.Token;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.List;

public class ClinicActivity extends MenuBase {
    ClinicTask clinicTask = new ClinicTask();
    ClinicModel clinicModel;
    ClinicDao clinicDao;
    PhoneNumberDao numberDao;
    RecyclerView recyclerView;
    DateTime upDate;
    TextView tvClinicName;
    TextView tvClinicOpenHours;
    TextView tvClinicTown;
    TextView tvClinicPostCode;
    TextView tvClinicStreet;
    TextView tvClinicBuilding;
    Button btnGoToEmployeeActivity;
    Boolean forceRedownload = false;
    Token token = Token.getInstance();
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        tvClinicName = findViewById(R.id.tvClinicName);
        tvClinicPostCode = findViewById(R.id.tvPostCode);
        tvClinicBuilding = findViewById(R.id.tvBuildingNr);
        tvClinicStreet = findViewById(R.id.tvStreet);
        tvClinicTown = findViewById(R.id.tvTown);
        tvClinicOpenHours = findViewById(R.id.tvOpenHours);
        recyclerView = findViewById(R.id.rvPhoneNumbers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        btnGoToEmployeeActivity = findViewById(R.id.btnGoToEmployeeActivity);
        btnGoToEmployeeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClinicActivity.this, EmployeeListActivity.class);
                startActivity(intent);
            }
        });
        if (token.getAccess_token() == null)
            logout();
        preferences = this.getSharedPreferences(token.getUserName(), Context.MODE_PRIVATE);
        long longDate = preferences.getLong("updateClinicTime", 0);
        upDate = Converters.fromTimestamp(longDate);
        clinicTask = new ClinicTask();
        clinicTask.execute();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.connectionIsUp(ClinicActivity.this)) {
                    forceRedownload = true;
                    clinicTask = new ClinicTask();
                    clinicTask.execute();
                } else {
                    Toast.makeText(ClinicActivity.this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new ClinicPhoneNumberRecyclerAdapter(clinicModel.getClinicPhoneNumber()));
    }

    public class ClinicTask extends AsyncTask<Void, Void, Integer> {

        ClinicTask() {
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int code = -1;
            clinicDao = ClinicDb.getDatabase(ClinicActivity.this).ClinicDao();
            numberDao = ClinicDb.getDatabase(ClinicActivity.this).PhoneNumberDao();
            int clinicId = preferences.getInt("prefClinic", 0);
            if (Days.daysBetween(upDate, DateTime.now()).getDays() > 3 && Connectivity.connectionIsUp(ClinicActivity.this) || forceRedownload && Connectivity.connectionIsUp(ClinicActivity.this)) {
                ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                Controller controller = new Controller(new ApiConnection(Settings.BASE_URL));
                try {
                    RestResponse response = controller.getClinic(String.valueOf(clinicId));
                    clinicModel = mapper.readValue(response.getResponse(), ClinicModel.class);
                    code = response.getResponseCode();
                    if (code == 200) {
                        clinicDao.insert(new ClinicRoomModel(clinicModel));
                        numberDao.insert(clinicModel.getClinicPhoneNumber().toArray(new PhoneNumberModel[clinicModel.getClinicPhoneNumber().size()]));
                    }
                    return code;
                } catch (Exception e) {
                    e.getMessage();
                }
            } else if (upDate != Converters.fromTimestamp(Long.valueOf(0))) {
                clinicModel = new ClinicModel(clinicDao.findClinicById(clinicId), new ArrayList<PhoneNumberModel>(numberDao.findNumberByClinic(clinicId)));
                forceRedownload = false;
                code = 200;
            }
            return code;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            if (success == 200) {
                tvClinicName.setText(clinicModel.getClinicName());
                tvClinicBuilding.setText(clinicModel.getAddress().getBuildingNr());
                tvClinicOpenHours.setText(clinicModel.getOpeningHours());
                tvClinicPostCode.setText(clinicModel.getAddress().getPostalCode());
                tvClinicStreet.setText(clinicModel.getAddress().getStreet());
                tvClinicTown.setText(clinicModel.getAddress().getTown());
                setupRecyclerView(recyclerView);
                preferences.edit().putLong("updateClinicTime", Converters.dateToTimestamp(DateTime.now())).apply();
                if (forceRedownload)
                    Toast.makeText(ClinicActivity.this, R.string.refreshed, Toast.LENGTH_SHORT).show();
                forceRedownload = false;
            } else {
                Toast.makeText(ClinicActivity.this, R.string.ToastCantConnect, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
