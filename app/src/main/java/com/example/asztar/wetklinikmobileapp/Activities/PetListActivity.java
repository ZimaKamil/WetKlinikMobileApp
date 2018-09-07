package com.example.asztar.wetklinikmobileapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asztar.wetklinikmobileapp.Adapters.PetRecyclerAdapter;
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
import com.example.asztar.wetklinikmobileapp.R;

import com.example.asztar.wetklinikmobileapp.Token;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An activity representing a list of Pets. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PetDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PetListActivity extends MenuBase {

    Token token = Token.getInstance();
    private SharedPreferences preferences;
    private PetsTask petTask;
    private ArrayList<PetModel> petModelArrayList;
    private RecyclerView recyclerView;
    private DateTime upDate;
    private PetDao petDao;
    private Boolean forceRedownload = false;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);
        recyclerView = findViewById(R.id.pet_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        assert recyclerView != null;
        preferences = this.getSharedPreferences(token.getUserName(), Context.MODE_PRIVATE);
        long longDate = preferences.getLong("updatePetListTime", 0);
        upDate = Converters.fromTimestamp(longDate);
        fab = findViewById(R.id.fabRefreshPetList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.connectionIsUp(PetListActivity.this)) {
                    forceRedownload = true;
                    petTask = new PetsTask();
                    petTask.execute();
                } else {
                    Toast.makeText(PetListActivity.this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (token.getAccess_token() == null)
            logout();
        petTask = new PetsTask();
        petTask.execute();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new PetRecyclerAdapter(petModelArrayList));
    }

    private class PetsTask extends AsyncTask<Void, Void, Integer> {
        PetsTask() {
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int code = -1;
            petDao = ClinicDb.getDatabase(PetListActivity.this).PetDao();
            if (Days.daysBetween(upDate, DateTime.now()).getDays() > 3 && Connectivity.connectionIsUp(PetListActivity.this) || forceRedownload && Connectivity.connectionIsUp(PetListActivity.this)) {
                ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.registerModule(new JodaModule());
                Controller controller = new Controller(new ApiConnection(Settings.BASE_URL));
                try {
                    RestResponse response = controller.getPets();
                    PetModel[] petModels = mapper.readValue(response.getResponse(), PetModel[].class);
                    petModelArrayList = new ArrayList<>(Arrays.asList(petModels));
                    code = response.getResponseCode();
                    if (code == 200) {
                        petDao.insert(petModelArrayList.toArray(new PetModel[petModelArrayList.size()]));
                        preferences.edit().putLong("updatePetListTime", Converters.dateToTimestamp(DateTime.now())).apply();
                    }
                    return code;
                } catch (Exception e) {
                    e.getMessage();
                }
            } else if (upDate != Converters.fromTimestamp(Long.valueOf(0))) {
                Token token = Token.getInstance();
                petModelArrayList = new ArrayList<PetModel>(petDao.getAllPetsOfUser(token.getUserName()));
                code = 200;
            }
            return code;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            if (success == 200) {
                //mAdapter = new PetRecyclerAdapter(petModelArrayList);
                //mRecyclerView.setAdapter(mAdapter);
                setupRecyclerView(recyclerView);
                if (forceRedownload)
                    Toast.makeText(PetListActivity.this, R.string.refreshed, Toast.LENGTH_SHORT).show();
                forceRedownload = false;
            } else {
                Toast.makeText(PetListActivity.this, R.string.ToastCantConnect, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
