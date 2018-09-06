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

import com.example.asztar.wetklinikmobileapp.Activities.dummy.dummy.DummyContent;
import com.example.asztar.wetklinikmobileapp.Token;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    SharedPreferences preferences;
    PetsTask petTask;
    ArrayList<PetModel> petModelArrayList;
    RecyclerView recyclerView;
    DateTime upDate;
    PetDao petDao;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        if (findViewById(R.id.pet_detail_container) != null) {
        }
        recyclerView = findViewById(R.id.pet_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        assert recyclerView != null;
        preferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        long longDate = preferences.getLong("updatePetListTime", 0);
        upDate = Converters.fromTimestamp(longDate);
        petDao = ClinicDb.getDatabase(PetListActivity.this).PetDao();
        fab = findViewById(R.id.fabRefreshPetList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Connectivity.connectionIsUp(PetListActivity.this)) {
                    petTask = new PetsTask();
                    petTask.execute();
                    Toast.makeText(PetListActivity.this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (Days.daysBetween(upDate, DateTime.now()).getDays()>3 && Connectivity.connectionIsUp(this)) {
            petTask = new PetsTask();
            petTask.execute();
        }
        else{
            Token token = Token.getInstance();
            petModelArrayList = new ArrayList<PetModel>(petDao.getAllPetsOfUser(token.getUserName()));
            setupRecyclerView(recyclerView);
        }
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
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Controller controller = new Controller(new ApiConnection(Settings.BASE_URL));
            try {
                RestResponse response = controller.getPets();
                PetModel[] petModels = mapper.readValue(response.Response, PetModel[].class);
                petModelArrayList = new ArrayList<>(Arrays.asList(petModels));
                return response.ResponseCode;
            }
            catch (Exception e){
                e.getMessage();
            }
            return code;
        }
        @Override
        protected void onPostExecute(final Integer success) {
            if (success == 200) {
                //mAdapter = new PetRecyclerAdapter(petModelArrayList);
                //mRecyclerView.setAdapter(mAdapter);
                preferences.edit().putLong("updatePetListTime", Converters.dateToTimestamp(DateTime.now())).apply();
                petDao.insert(petModelArrayList.toArray(new PetModel[petModelArrayList.size()]));
                setupRecyclerView(recyclerView);
                Toast.makeText(PetListActivity.this, success.toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PetListActivity.this, success.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
