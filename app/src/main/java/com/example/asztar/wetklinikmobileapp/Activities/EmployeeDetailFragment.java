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
import com.example.asztar.wetklinikmobileapp.Activities.dummy.DummyContent;

/**
 * A fragment representing a single Employee detail screen.
 * This fragment is either contained in a {@link EmployeeListActivity}
 * in two-pane mode (on tablets) or a {@link EmployeeDetailActivity}
 * on handsets.
 */
public class EmployeeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private TextView tvEmployeePosition, tvEmployeeName, tvEmployeeSurname, tvEmployeeDesc;
    private EmployeeModel mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
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
        tvEmployeePosition = (TextView) rootView.findViewById(R.id.tvEmployeePositionDetails);
        tvEmployeeName = (TextView) rootView.findViewById(R.id.tvEmployeeNameDetails);
        tvEmployeeSurname = (TextView) rootView.findViewById(R.id.tvEmployeeSurnameDetails);
        tvEmployeeDesc = (TextView) rootView.findViewById(R.id.tvEmployeeDescDetails);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            tvEmployeePosition.setText(mItem.Position);
            tvEmployeeName.setText(mItem.Name);
            tvEmployeeSurname.setText(mItem.Surname);
            tvEmployeeDesc.setText(mItem.Desc);
        }

        return rootView;
    }
}
