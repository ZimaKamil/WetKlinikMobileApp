package com.example.asztar.wetklinikmobileapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.asztar.wetklinikmobileapp.Activities.ClinicActivity;
import com.example.asztar.wetklinikmobileapp.Activities.DatatableActivity;
import com.example.asztar.wetklinikmobileapp.Activities.PetListActivity;
import com.example.asztar.wetklinikmobileapp.Activities.SettingsActivity;

public abstract class MenuBase extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_employee, menu);
        invalidateOptionsMenu();
        MenuItem item = menu.findItem(R.id.action_settings);
        if (this.getClass() == ClinicActivity.class)
            item = menu.findItem(R.id.action_clinic);
        else if (this.getClass() == DatatableActivity.class)
            item = menu.findItem(R.id.action_calendar);
        else if (this.getClass() == PetListActivity.class)
            item = menu.findItem(R.id.action_pets);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                finish();
                return true;
            case R.id.action_pets:
                Intent intent = new Intent(this, PetListActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_calendar:
                Intent intentCalendar = new Intent(this, DatatableActivity.class);
                startActivity(intentCalendar);
                finish();
                return true;
            case R.id.action_clinic:
                Intent intentClinic = new Intent(this, ClinicActivity.class);
                startActivity(intentClinic);
                finish();
                return true;
            case R.id.action_sign_out:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout(){
        Token.reset();
        Intent intentLogout = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        intentLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentLogout);
    }
}
