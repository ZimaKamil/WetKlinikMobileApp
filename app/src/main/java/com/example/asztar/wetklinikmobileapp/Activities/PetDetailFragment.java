package com.example.asztar.wetklinikmobileapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.asztar.wetklinikmobileapp.Connectivity;
import com.example.asztar.wetklinikmobileapp.Models.ClinicDb;
import com.example.asztar.wetklinikmobileapp.Models.Converters;
import com.example.asztar.wetklinikmobileapp.Models.PetDao;
import com.example.asztar.wetklinikmobileapp.Models.PetModel;
import com.example.asztar.wetklinikmobileapp.Models.PetTreatmentDao;
import com.example.asztar.wetklinikmobileapp.Models.PetTreatmentModel;
import com.example.asztar.wetklinikmobileapp.R;
import com.example.asztar.wetklinikmobileapp.Token;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import org.joda.time.DateTime;
import org.joda.time.Days;

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

    Token token = Token.getInstance();
    private SharedPreferences preferences;
    private DateTime upDate;
    private EditDesc editDesc;
    private PetModel mItem;
    private TextView tvPetName, tvPetBreed, tvPetSpecies, tvPetHeight, tvPetLength, tvPetSex, tvPetBirthdate;
    private Button btnDownloadPetTreatment;
    private EditText etPetDesc;
    private DownloadPetTreatment downloadPetTreatment;
    PetDao petDao;

    public PetDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            petDao = ClinicDb.getDatabase(getActivity()).PetDao();
            mItem = petDao.findPetById(Integer.valueOf(getArguments().getString(ARG_ITEM_ID)));
            preferences = getActivity().getSharedPreferences(token.getUserName(), Context.MODE_PRIVATE);
            long longDate = preferences.getLong("updatePetTreatmentId"+getArguments().getString(ARG_ITEM_ID)+"Time", 0);
            upDate = Converters.fromTimestamp(longDate);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pet_detail, container, false);
        tvPetName = rootView.findViewById(R.id.tvPetNameDetails);
        tvPetBreed = rootView.findViewById(R.id.tvBreedDetails);
        tvPetSpecies = rootView.findViewById(R.id.tvSpeciesDetails);
        tvPetHeight = rootView.findViewById(R.id.tvPetHeight);
        tvPetLength = rootView.findViewById(R.id.tvPetLength);
        etPetDesc = rootView.findViewById(R.id.etDesc);
        tvPetSex = rootView.findViewById(R.id.tvSexDetails);
        tvPetBirthdate = rootView.findViewById(R.id.tvBirthdateDetails);
        btnDownloadPetTreatment = rootView.findViewById(R.id.btnDonwloadPetTreatments);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(mItem.getBirthDate().getYear())).append(".");
            if (mItem.getBirthDate().getMonthOfYear()<10)
                sb.append("0").append(mItem.getBirthDate().getMonthOfYear()).append(".");
            else {
                sb.append(mItem.getBirthDate().getMonthOfYear()).append(".");
            }
            if(mItem.getBirthDate().getDayOfMonth()<10)
                sb.append("0").append(mItem.getBirthDate().getDayOfMonth());
            else{sb.append(mItem.getBirthDate().getDayOfMonth());}
            if (mItem.getLength() != 0)
                tvPetLength.setText(mItem.getLength().toString());
            else{tvPetLength.setText("-");}
            if (mItem.getHeight() != 0)
                tvPetHeight.setText(mItem.getHeight().toString());
            else{tvPetHeight.setText("-");}
            if(mItem.getSex()==1)
                tvPetSex.setText("Samiec");
            else{tvPetSex.setText("Samica");}
            tvPetBirthdate.setText(sb.toString());
            tvPetBreed.setText(mItem.getPetBreed());
            tvPetSpecies.setText(mItem.getPetSpecies());
            tvPetName.setText(mItem.getPetName());
            etPetDesc.setText(mItem.getClientDesc());
            btnDownloadPetTreatment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadPetTreatment = new DownloadPetTreatment();
                    downloadPetTreatment.execute();
                }
            });
            etPetDesc.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
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
                Boolean response = controller.putPet(mItem.getPetId().toString(), etPetDesc.getText().toString());
                if (response == true)
                    mItem.setClientDesc(etPetDesc.getText().toString());
                    petDao.update(mItem);
                return response;
            } catch (Exception e) {
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
            if (Days.daysBetween(upDate, DateTime.now()).getDays() > 3 && Connectivity.connectionIsUp(getActivity())) {
                ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                Controller controller = new Controller(new ApiConnection(Settings.BASE_URL));
                mapper.registerModule(new JodaModule());
                try {
                    RestResponse response = controller.getPetTreatment(mItem.getPetId().toString());
                    PetTreatmentModel[] petTreatmentModels = mapper.readValue(response.getResponse(), PetTreatmentModel[].class);
                    ArrayList<PetTreatmentModel> petTreatmentArrayList = new ArrayList<PetTreatmentModel>(Arrays.asList(petTreatmentModels));
                    code = response.getResponseCode();
                    if (code == 200) {
                        PetTreatmentDao petTreatmentDao = ClinicDb.getDatabase(getActivity()).PetTreatmentDao();
                        petTreatmentDao.insert(petTreatmentArrayList.toArray(new PetTreatmentModel[petTreatmentArrayList.size()]));
                    }
                    return code;
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            else if(upDate != Converters.fromTimestamp(Long.valueOf(0))){
                code = 200;
        }
        return code;
    }

        @Override
        protected void onPostExecute(final Integer success) {

            if (success == 200) {
                Intent intent = new Intent(getActivity(), PetTreatmentActivity.class);
                intent.putExtra(PetDetailFragment.ARG_ITEM_ID, mItem.getPetId().toString());
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Nie udało się pobrać karty zdrowia", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
