package com.example.asztar.wetklinikmobileapp.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asztar.wetklinikmobileapp.ApiConn.ApiConnection;
import com.example.asztar.wetklinikmobileapp.ApiConn.Controller;
import com.example.asztar.wetklinikmobileapp.ApiConn.RestResponse;
import com.example.asztar.wetklinikmobileapp.ApiConn.Settings;
import com.example.asztar.wetklinikmobileapp.Models.ClinicDb;
import com.example.asztar.wetklinikmobileapp.Models.PetDao;
import com.example.asztar.wetklinikmobileapp.Models.PetModel;
import com.example.asztar.wetklinikmobileapp.Models.PetTreatmentModel;
import com.example.asztar.wetklinikmobileapp.R;
import com.example.asztar.wetklinikmobileapp.Activities.dummy.dummy.DummyContent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A fragment representing a single Pet detail screen.
 * This fragment is either contained in a {@link PetListActivity}
 * in two-pane mode (on tablets) or a {@link PetDetailActivity}
 * on handsets.
 */
public class PetDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private EditDesc editDesc;
    private PetModel mItem;
    private TextView tvPetName, tvPetBreed, tvPetSpecies, tvPetHeight, tvPetLength;
    private Button btnDownloadPetTreatment;
    private EditText etPetDesc;
    private DownloadPetTreatment downloadPetTreatment;

    public PetDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            PetDao petDao = ClinicDb.getDatabase(getActivity()).PetDao();
            mItem = petDao.findPetById(Integer.valueOf(getArguments().getString(ARG_ITEM_ID)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pet_detail, container, false);
        tvPetName = (TextView) rootView.findViewById(R.id.tvPetNameDetails);
        tvPetBreed = (TextView) rootView.findViewById(R.id.tvBreedDetails);
        tvPetSpecies = (TextView) rootView.findViewById(R.id.tvSpeciesDetails);
        tvPetHeight = (TextView) rootView.findViewById(R.id.tvPetHeight);
        tvPetLength = (TextView) rootView.findViewById(R.id.tvPetLength);
        etPetDesc = (EditText) rootView.findViewById(R.id.etDesc);
        btnDownloadPetTreatment = (Button) rootView.findViewById(R.id.btnGoToEmployeeActivity);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            tvPetBreed.setText(mItem.PetBreed);
            tvPetSpecies.setText(mItem.PetSpecies);
            tvPetName.setText(mItem.Name);
            tvPetHeight.setText(mItem.Height.toString());
            tvPetLength.setText(mItem.Length.toString());
            etPetDesc.setText(mItem.ClientDesc);
            btnDownloadPetTreatment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadPetTreatment = new DownloadPetTreatment();
                    downloadPetTreatment.execute();
                }
            });
            etPetDesc.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        editDesc = new EditDesc();
                        editDesc.execute();
                        return true;
                    }
                    return false;
                }
            });
        }
        return rootView;
    }

    public class EditDesc extends AsyncTask<Void, Void, Boolean> {

        EditDesc() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int code = -1;
            Controller controller = new Controller(new ApiConnection(Settings.BASE_URL));
            try {
                Boolean response = controller.putPet(mItem.PetId.toString(), etPetDesc.getText().toString());
                return response;
            }
            catch (Exception e){
                e.getMessage();
            }
            return false;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if (success == true) {
                Toast.makeText(getActivity(), "Wysłano dodatkowy opis", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Nie udało się wysłać opisu", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

    public class DownloadPetTreatment extends AsyncTask<Void, Void, Integer> {

        DownloadPetTreatment() {
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int code = -1;
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Controller controller = new Controller(new ApiConnection(Settings.BASE_URL));
            try {
                RestResponse response = controller.getPetTreatment(mItem.PetId.toString());
                PetTreatmentModel[] petTreatmentModels = mapper.readValue(response.Response, PetTreatmentModel[].class);
                ArrayList<PetTreatmentModel> PetTreatmentArrayList = new ArrayList<PetTreatmentModel>(Arrays.asList(petTreatmentModels)) ;
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
                Toast.makeText(getActivity(), "Pobrano kartę zdrowia", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Nie udało się pobrać karty zdrowia", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected void onCancelled() {

        }
    }
}
