package com.example.asztar.wetklinikmobileapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asztar.wetklinikmobileapp.Models.PetTreatmentModel;
import com.example.asztar.wetklinikmobileapp.R;

import java.util.ArrayList;

public class PetTreatmentRecyclerAdapter extends RecyclerView.Adapter<PetTreatmentRecyclerAdapter.MyViewHolder> {

    private ArrayList<PetTreatmentModel> PetTreatmentModels;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView treatmentDate, treatmentName, treatmentDesc, treatmentEmployee;

        private MyViewHolder(View view) {
            super(view);
            //petId = (TextView) view.findViewById(R.id.tvPetId);
            treatmentDate = view.findViewById(R.id.tvPetTreatmentDate);
            treatmentName = view.findViewById(R.id.tvPetTreatmentPetName);
            treatmentEmployee = view.findViewById(R.id.tvPetTreatmentEmployee);
        }
    }

    public PetTreatmentRecyclerAdapter(ArrayList<PetTreatmentModel> petTreatmentModels) {
        PetTreatmentModels = petTreatmentModels;
    }

    @Override
    public PetTreatmentRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_pet_treatment_content, parent, false);
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(PetTreatmentModels.get(position).getDate().getYear())).append(".");
        if (PetTreatmentModels.get(position).getDate().getMonthOfYear()<10)
                sb.append("0").append(PetTreatmentModels.get(position).getDate().getMonthOfYear()).append(".");
        else {
            sb.append(PetTreatmentModels.get(position).getDate().getMonthOfYear()).append(".");
        }
        if(PetTreatmentModels.get(position).getDate().getDayOfMonth()<10)
                sb.append("0").append(PetTreatmentModels.get(position).getDate().getDayOfMonth());
        else{sb.append(PetTreatmentModels.get(position).getDate().getDayOfMonth());}
        StringBuilder sbEmployee = new StringBuilder();
        sbEmployee.append(PetTreatmentModels.get(position).getEmployeePetTreatment().getPosition().substring(0,3)).append(".")
                .append(PetTreatmentModels.get(position).getEmployeePetTreatment().getEmployeeName()).append(" ")
                .append(PetTreatmentModels.get(position).getEmployeePetTreatment().getSurname());
        holder.treatmentDate.setText(sb.toString());
        holder.treatmentName.setText(PetTreatmentModels.get(position).getTreatment().getTreatmentName());
        holder.treatmentEmployee.setText(sbEmployee.toString());
    }

    @Override
    public int getItemCount() {
        return PetTreatmentModels.size();
    }
}
