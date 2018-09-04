package com.example.asztar.wetklinikmobileapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asztar.wetklinikmobileapp.ApiConn.ApiConnection;
import com.example.asztar.wetklinikmobileapp.ApiConn.RestResponse;
import com.example.asztar.wetklinikmobileapp.Models.ClinicModel;
import com.example.asztar.wetklinikmobileapp.Models.EmployeeModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

public class ClinicActivity extends MenuBase {
ClinicTask clinicTask = new ClinicTask();
ClinicModel clinicModel = new ClinicModel();

TextView tvClinicName;
Button btnGoToEmployeeActivity;

SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        tvClinicName = (TextView) findViewById(R.id.tvClinicName);
        btnGoToEmployeeActivity = (Button) findViewById(R.id.btnGoToEmployeeActivity);
        btnGoToEmployeeActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClinicActivity.this, EmployeeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        preferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clinicTask = new ClinicTask();
                clinicTask.execute();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public class ClinicTask extends AsyncTask<Void, Void, Integer> {

        ClinicTask() {
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int code = -1;
            int x = preferences.getInt("prefClinic", 0);
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Controller controller = new Controller(new ApiConnection(Settings.BASE_URL));
            try {
                RestResponse response = controller.getClinic(String.valueOf(x));
                clinicModel = mapper.readValue(response.Response, ClinicModel.class);
                return response.ResponseCode;
            }
            catch (Exception e){
                e.getMessage();
            }
            return code;
        }
        @Override
        protected void onPostExecute(final Integer success) {
            //showProgress(false);

            if (success == 200) {

                tvClinicName.setText(clinicModel.name);
                Toast.makeText(ClinicActivity.this, clinicModel.address.getTown(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ClinicActivity.this, success.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            //showProgress(false);
        }
    }

    public void sendNotification(View view) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);

        //Create the intent that’ll fire when the user taps the notification//

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        mBuilder.setContentTitle("Klinika weterynaryjna");
        mBuilder.setContentText("Umówione spotkanie o ");

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }


}
