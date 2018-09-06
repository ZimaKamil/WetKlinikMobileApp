package com.example.asztar.wetklinikmobileapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asztar.wetklinikmobileapp.ApiConn.ApiConnection;
import com.example.asztar.wetklinikmobileapp.ApiConn.Controller;
import com.example.asztar.wetklinikmobileapp.ApiConn.RestResponse;
import com.example.asztar.wetklinikmobileapp.ApiConn.Settings;
import com.example.asztar.wetklinikmobileapp.Connectivity;
import com.example.asztar.wetklinikmobileapp.MenuBase;
import com.example.asztar.wetklinikmobileapp.Models.ClinicDb;
import com.example.asztar.wetklinikmobileapp.Models.Converters;
import com.example.asztar.wetklinikmobileapp.Models.EmployeeDao;
import com.example.asztar.wetklinikmobileapp.Models.EmployeeModel;
import com.example.asztar.wetklinikmobileapp.R;

import com.example.asztar.wetklinikmobileapp.Activities.dummy.DummyContent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An activity representing a list of Employees. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EmployeeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class EmployeeListActivity extends MenuBase {
    private boolean mTwoPane;
    SharedPreferences preferences;
    View recyclerView;
    DateTime upDate;
    EmployeeTask employeeTask;
    EmployeeDao employeeDao;
    Integer prefClinic;
    FloatingActionButton fab;
    ArrayList<EmployeeModel> employeeModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
        preferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        fab = findViewById(R.id.fabRefreshEmployeeList);
        prefClinic = preferences.getInt("prefClinic", 0);
        long longDate = preferences.getLong("updateEmployeeListTime", 0);
        upDate = Converters.fromTimestamp(longDate);
        employeeDao = ClinicDb.getDatabase(this).EmployeeDao();
        if (Days.daysBetween(upDate, DateTime.now()).getDays()>3 && Connectivity.connectionIsUp(this)) {
            employeeTask = new EmployeeTask();
            employeeTask.execute();
        }
        else {
            List<EmployeeModel> employee = employeeDao.findEmployeeByClinic(prefClinic);
            employeeModelArrayList = new ArrayList<>(employee);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Connectivity.connectionIsUp(EmployeeListActivity.this)) {
                    employeeTask = new EmployeeTask();
                    employeeTask.execute();
                }
                Toast.makeText(EmployeeListActivity.this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
            }
        });
        View recyclerView = findViewById(R.id.employee_list);
        assert recyclerView != null;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, employeeModelArrayList));
    }

    public class EmployeeTask extends AsyncTask<Void, Void, Integer> {

        EmployeeTask() {
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int code = -1;
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Controller controller = new Controller(new ApiConnection(Settings.BASE_URL));
            try {
                RestResponse response = controller.getEmployees(prefClinic.toString());
                EmployeeModel[] employeeModels = mapper.readValue(response.Response, EmployeeModel[].class);
                employeeModelArrayList = new ArrayList<>(Arrays.asList(employeeModels));
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
                preferences.edit().putLong("updateEmployeeListTime", Converters.dateToTimestamp(DateTime.now())).apply();
                employeeDao.insert(employeeModelArrayList.toArray(new EmployeeModel[employeeModelArrayList.size()]));
                setupRecyclerView((RecyclerView) recyclerView);
                Toast.makeText(EmployeeListActivity.this, success.toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EmployeeListActivity.this, success.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final EmployeeListActivity mParentActivity;
        private final ArrayList<EmployeeModel> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmployeeModel employeeModel = (EmployeeModel) view.getTag();
                Context context = view.getContext();
                Intent intent = new Intent(context, EmployeeDetailActivity.class);
                intent.putExtra(EmployeeDetailFragment.ARG_ITEM_ID, employeeModel.EmployeeId.toString());
                context.startActivity(intent);
            }
        };

        SimpleItemRecyclerViewAdapter(EmployeeListActivity parent, ArrayList<EmployeeModel> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.employee_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.tvEmployeePosition.setText(mValues.get(position).EmployeeId);
            holder.tvEmployeeName.setText(mValues.get(position).Name);
            holder.tvEmployeeName.setText(mValues.get(position).Surname);
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView tvEmployeePosition;
            final TextView tvEmployeeName;
            final TextView tvEmployeeSurname;

            ViewHolder(View view) {
                super(view);
                tvEmployeePosition = view.findViewById(R.id.tvEmployeePosition);
                tvEmployeeName = view.findViewById(R.id.tvEmployeeName);
                tvEmployeeSurname = view.findViewById(R.id.tvEmployeeSurname);

            }
        }
    }
}
