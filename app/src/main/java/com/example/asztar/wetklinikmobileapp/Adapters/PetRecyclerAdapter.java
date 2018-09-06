package com.example.asztar.wetklinikmobileapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asztar.wetklinikmobileapp.Activities.PetDetailActivity;
import com.example.asztar.wetklinikmobileapp.Activities.PetDetailFragment;
import com.example.asztar.wetklinikmobileapp.Activities.PetListActivity;
import com.example.asztar.wetklinikmobileapp.Models.PetModel;
import com.example.asztar.wetklinikmobileapp.R;

import java.util.ArrayList;

public class PetRecyclerAdapter extends RecyclerView.Adapter<PetRecyclerAdapter.MyViewHolder> {

    private ArrayList<PetModel> PetModelList;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PetModel pet = (PetModel) view.getTag();
                Context context = view.getContext();
                Intent intent = new Intent(context, PetDetailActivity.class);
                intent.putExtra(PetDetailFragment.ARG_ITEM_ID, pet.PetId.toString());
                context.startActivity(intent);
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView petId, petSpecies, petBreed, petName;

        private MyViewHolder(View view) {
            super(view);
            //petId = (TextView) view.findViewById(R.id.tvPetId);
            petSpecies = view.findViewById(R.id.tvPetSpieces);
            petBreed = view.findViewById(R.id.tvPetBreed);
            petName = view.findViewById(R.id.tvPetName);
        }
    }
    public PetRecyclerAdapter(ArrayList<PetModel> petModelList) {
        PetModelList = petModelList;
    }

    @Override
    public PetRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pet_list_content, parent, false);
        return  new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //holder.petId.setText(PetModelList.get(position).PetId);
        holder.petName.setText(PetModelList.get(position).Name);
        holder.petBreed.setText(PetModelList.get(position).PetBreed);
        holder.petSpecies.setText(PetModelList.get(position).PetSpecies);

        holder.itemView.setTag(PetModelList.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }
    @Override
    public int getItemCount() {
        return PetModelList.size();
    }
}
