package com.example.asztar.wetklinikmobileapp.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asztar.wetklinikmobileapp.Models.ClinicDb;
import com.example.asztar.wetklinikmobileapp.Models.EmployeeDao;
import com.example.asztar.wetklinikmobileapp.Models.EmployeeModel;
import com.example.asztar.wetklinikmobileapp.R;

public class EmployeeDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private TextView tvEmployeePosition, tvEmployeeName, tvEmployeeSurname, tvEmployeeDesc;
    private EmployeeModel mItem;

    public EmployeeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            EmployeeDao employeeDao = ClinicDb.getDatabase(getActivity()).EmployeeDao();
            mItem = employeeDao.findEmployeeById(Integer.valueOf(getArguments().getString(ARG_ITEM_ID)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.employee_detail, container, false);
        tvEmployeePosition = rootView.findViewById(R.id.tvEmployeePositionDetails);
        tvEmployeeName = rootView.findViewById(R.id.tvEmployeeNameDetails);
        tvEmployeeSurname = rootView.findViewById(R.id.tvEmployeeSurnameDetails);
        tvEmployeeDesc = rootView.findViewById(R.id.tvEmployeeDescDetails);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            tvEmployeePosition.setText(mItem.getPosition());
            tvEmployeeName.setText(mItem.getEmployeeName());
            tvEmployeeSurname.setText(mItem.getSurname());
            tvEmployeeDesc.setText(mItem.getEmployeeDesc());
        }

        return rootView;
    }
}
