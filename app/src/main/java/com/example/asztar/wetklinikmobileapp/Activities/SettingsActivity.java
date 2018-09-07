package com.example.asztar.wetklinikmobileapp.Activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asztar.wetklinikmobileapp.ApiConn.Settings;
import com.example.asztar.wetklinikmobileapp.ChangePassword;
import com.example.asztar.wetklinikmobileapp.Connectivity;
import com.example.asztar.wetklinikmobileapp.MenuBase;
import com.example.asztar.wetklinikmobileapp.R;
import com.example.asztar.wetklinikmobileapp.Token;

import java.util.List;
import java.util.logging.Handler;

public class SettingsActivity extends MenuBase {

    Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Token token = Token.getInstance();
        setContentView(R.layout.activity_settings);
        Button btnChangePassword = findViewById(R.id.btnChangePassword);
        Button btnClearPassword = findViewById(R.id.btnClearPassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity.connectionIsUp(SettingsActivity.this)) {
                    ChangePassword password = new ChangePassword(SettingsActivity.this);
                    password.change();
                } else {
                    Toast.makeText(SettingsActivity.this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
                }
                if (token.getAccess_token() == null)
                    logout();
            }
        });
        /*btnClearPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = SettingsActivity.this.getSharedPreferences("user", Context.MODE_PRIVATE);
                preferences.edit().putBoolean("RememberMe", false).commit();
                AccountManager accountManager = AccountManager.get(SettingsActivity.this);
                Account account = accountManager.getAccountsByType("klinik.wet")[0];
                accountManager.invalidateAuthToken("klinik.wet", token.getAccess_token());
            }
        });*/
    }
}