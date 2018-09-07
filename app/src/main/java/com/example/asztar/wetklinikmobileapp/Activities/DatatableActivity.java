package com.example.asztar.wetklinikmobileapp.Activities;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asztar.wetklinikmobileapp.ApiConn.ApiConnection;
import com.example.asztar.wetklinikmobileapp.ApiConn.Controller;
import com.example.asztar.wetklinikmobileapp.ApiConn.RestResponse;
import com.example.asztar.wetklinikmobileapp.ApiConn.Settings;
import com.example.asztar.wetklinikmobileapp.Connectivity;
import com.example.asztar.wetklinikmobileapp.EventDecorator;
import com.example.asztar.wetklinikmobileapp.MenuBase;
import com.example.asztar.wetklinikmobileapp.Models.AppointmentDao;
import com.example.asztar.wetklinikmobileapp.Models.AppointmentModel;
import com.example.asztar.wetklinikmobileapp.Models.ClinicDb;
import com.example.asztar.wetklinikmobileapp.Models.Converters;
import com.example.asztar.wetklinikmobileapp.R;
import com.example.asztar.wetklinikmobileapp.Token;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


public class DatatableActivity extends MenuBase {

    CalendarTask calendarTask = new CalendarTask();
    ArrayList<AppointmentModel> appointmentModelArrayList;
    DateTime upDate;
    MaterialCalendarView calendar;
    Button btnGoToEmployeeActivity;
    Boolean forceRedownload = false;
    AppointmentDao appointmentDao;
    SharedPreferences preferences;
    Token token = Token.getInstance();
    ArrayList<CalendarDay> dates = new ArrayList<>();
    Map<String, Integer> index;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datatable);
        FloatingActionButton fab = findViewById(R.id.fab);
        preferences = this.getSharedPreferences(token.getUserName(), Context.MODE_PRIVATE);
        long longDate = preferences.getLong("updateCalendarTime", 0);
        upDate = Converters.fromTimestamp(longDate);
        calendarTask = new CalendarTask();
        calendarTask.execute();
        calendar = findViewById(R.id.calendarView);
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {
                String doy = String.valueOf(date.getDate().getYear()) + String.valueOf(date.getDate().getDayOfYear());
                if (index.containsKey(doy)) {
                    final AppointmentModel appointment = appointmentModelArrayList.get(index.get(doy));
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.valueOf(appointment.getDate().getYear())).append(".");
                    if (appointment.getDate().getMonthOfYear()<10)
                        sb.append("0").append(appointment.getDate().getMonthOfYear()).append(".");
                    else {
                        sb.append(appointment.getDate().getMonthOfYear()).append(".");
                    }
                    if(appointment.getDate().getDayOfMonth()<10)
                        sb.append("0").append(appointment.getDate().getDayOfMonth());
                    else{sb.append(appointment.getDate().getDayOfMonth());}
                    TextView day = new TextView(DatatableActivity.this);
                    day.setText(sb.toString());
                    TextView hour = new TextView(DatatableActivity.this);
                    if (appointment.getDate().getMinuteOfHour()<10)
                        hour.setText(String.valueOf(appointment.getDate().getHourOfDay()) + ":0" + String.valueOf(appointment.getDate().getMinuteOfHour()));
                    else{hour.setText(String.valueOf(appointment.getDate().getHourOfDay()) + ":" + String.valueOf(appointment.getDate().getMinuteOfHour())); }
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DatatableActivity.this);
                    alertDialog.setTitle("Umówiona wizyta");
                    LinearLayout linearLayout = new LinearLayout(DatatableActivity.this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    llp.setMargins(20, 20, 0, 0);
                    day.setLayoutParams(llp);
                    hour.setLayoutParams(llp);
                    linearLayout.addView(day);
                    linearLayout.addView(hour);
                    alertDialog.setView(linearLayout);
                    if (appointment.getDate().isAfterNow()) {
                        alertDialog.setPositiveButton("Dodaj powiadomienie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                PushReminder.initActivityObj(DatatableActivity.this);
                                PushReminder.IcsMakeNewCalendarEntry("Wizyta u weterynarza", "", "", Converters.dateToTimestamp(appointment.getDate().minusDays(1)), Converters.dateToTimestamp(appointment.getDate()), 1, 1, 1, 1);
                            }
                        });
                    }
                    alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    alert = alertDialog.create();
                    alert.show();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.connectionIsUp(DatatableActivity.this)) {
                    forceRedownload = true;
                    calendarTask = new CalendarTask();
                    calendarTask.execute();
                } else {
                    Toast.makeText(DatatableActivity.this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (token.getAccess_token() == null)
            logout();
    }


    public class CalendarTask extends AsyncTask<Void, Void, Integer> {

        CalendarTask() {
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int code = -1;
            appointmentDao = ClinicDb.getDatabase(DatatableActivity.this).AppointmentDao();
            int clinicId = preferences.getInt("prefClinic", 0);
            if (Days.daysBetween(upDate, DateTime.now()).getDays() > 1 && Connectivity.connectionIsUp(DatatableActivity.this) || forceRedownload && Connectivity.connectionIsUp(DatatableActivity.this)) {
                ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.registerModule(new JodaModule());
                Controller controller = new Controller(new ApiConnection(Settings.BASE_URL));
                try {
                    RestResponse response = controller.getAppointments();
                    AppointmentModel[] appointmentArray = mapper.readValue(response.getResponse(), AppointmentModel[].class);
                    appointmentModelArrayList = new ArrayList<AppointmentModel>(Arrays.asList(appointmentArray));
                    code = response.getResponseCode();
                    if (code == 200) {
                        appointmentDao.insert(appointmentArray);
                    }
                    return code;
                } catch (Exception e) {
                    e.getMessage();
                }
            } else if (upDate != Converters.fromTimestamp(Long.valueOf(0))) {
                appointmentModelArrayList = new ArrayList<AppointmentModel>(appointmentDao.findAppointmentByUser(token.getUserName()));
                code = 200;
            }
            return code;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            //showProgress(false);

            if (success == 200) {
                if (forceRedownload)
                    Toast.makeText(DatatableActivity.this, R.string.refreshed, Toast.LENGTH_SHORT).show();
                forceRedownload = false;
                index = new HashMap<String, Integer>();
                for (int i = 0; i < appointmentModelArrayList.size(); i++) {
                    index.put(String.valueOf(appointmentModelArrayList.get(i).getDate().year().get()) + String.valueOf(appointmentModelArrayList.get(i).getDate().dayOfYear().get()), i);
                    dates.add(CalendarDay.from(appointmentModelArrayList.get(i).getDate().year().get(), appointmentModelArrayList.get(i).getDate().monthOfYear().get(), appointmentModelArrayList.get(i).getDate().dayOfMonth().get()));
                }
                calendar.addDecorator(new EventDecorator(Color.RED, dates));
                preferences.edit().putLong("updateCalendarTime", Converters.dateToTimestamp(DateTime.now())).apply();
            } else {
                Toast.makeText(DatatableActivity.this, R.string.ToastCantConnect , Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
