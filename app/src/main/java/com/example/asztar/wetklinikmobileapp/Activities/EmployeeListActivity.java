package com.example.asztar.wetklinikmobileapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Activity;
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
import com.example.asztar.wetklinikmobileapp.MenuBase;
import com.example.asztar.wetklinikmobileapp.Models.EmployeeModel;
import com.example.asztar.wetklinikmobileapp.R;

import com.example.asztar.wetklinikmobileapp.Activities.dummy.DummyContent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    SharedPreferences preferences;
    View recyclerView;
    ArrayList<EmployeeModel> employeeModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        if (findViewById(R.id.employee_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.employee_list);
        assert recyclerView != null;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, employeeModelArrayList, mTwoPane));
    }

    public class ClinicTask extends AsyncTask<Void, Void, Integer> {

        ClinicTask() {
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int code = -1;
            int x = preferences.getInt("prefClinic", 1);
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Controller controller = new Controller(new ApiConnection(Settings.BASE_URL));
            try {
                RestResponse response = controller.getEmployees(String.valueOf(x));
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
                setupRecyclerView((RecyclerView) recyclerView);
                Toast.makeText(EmployeeListActivity.this, success.toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EmployeeListActivity.this, success.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            //showProgress(false);
        }
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final EmployeeListActivity mParentActivity;
        private final ArrayList<EmployeeModel> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(EmployeeDetailFragment.ARG_ITEM_ID, item.id);
                    EmployeeDetailFragment fragment = new EmployeeDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.employee_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, EmployeeDetailActivity.class);
                    intent.putExtra(EmployeeDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(EmployeeListActivity parent, ArrayList<EmployeeModel> items, boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
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
                tvEmployeePosition = (TextView) view.findViewById(R.id.tvEmployeePosition);
                tvEmployeeName = (TextView) view.findViewById(R.id.tvEmployeeName);
                tvEmployeeSurname = (TextView) view.findViewById(R.id.tvEmployeeSurname);

            }
        }
    }
}
