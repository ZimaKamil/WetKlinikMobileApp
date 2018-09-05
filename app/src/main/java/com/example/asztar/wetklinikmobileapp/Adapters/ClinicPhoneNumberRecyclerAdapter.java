package com.example.asztar.wetklinikmobileapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asztar.wetklinikmobileapp.Models.PhoneNumberModel;
import com.example.asztar.wetklinikmobileapp.R;

import java.util.ArrayList;

public class ClinicPhoneNumberRecyclerAdapter extends RecyclerView.Adapter<ClinicPhoneNumberRecyclerAdapter.MyViewHolder> {

    private ArrayList<PhoneNumberModel> PhoneNumberArrayList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView phoneNumber;

        public MyViewHolder(View view) {
            super(view);
            phoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);
        }
    }
    public ClinicPhoneNumberRecyclerAdapter(ArrayList<PhoneNumberModel> phoneNumbers) {
        PhoneNumberArrayList = phoneNumbers;
    }

    @Override
    public ClinicPhoneNumberRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clinic_recycler, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.phoneNumber.setText(PhoneNumberArrayList.get(position).Number);
    }
    @Override
    public int getItemCount() {
        return PhoneNumberArrayList.size();
    }
}
