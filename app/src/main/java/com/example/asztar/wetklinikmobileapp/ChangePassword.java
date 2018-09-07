package com.example.asztar.wetklinikmobileapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asztar.wetklinikmobileapp.ApiConn.ApiConnection;
import com.example.asztar.wetklinikmobileapp.ApiConn.Controller;
import com.example.asztar.wetklinikmobileapp.ApiConn.RestResponse;
import com.example.asztar.wetklinikmobileapp.ApiConn.Settings;

import java.util.ArrayList;
import java.util.Arrays;


public class ChangePassword {
    Button button;
    PasswordTask changePassTask;
    private AlertDialog alert;
    ConnectivityManager cm;
    private Context context;
    private EditText oldPass;
    private EditText newPass;
    private EditText confirmPass;

    public ChangePassword(Context context) {
        this.context = context;
    }

    public void change() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.dialogChangePassword);
        oldPass = new EditText(context);
        newPass = new EditText(context);
        confirmPass = new EditText(context);
        oldPass.setHint(R.string.etOldPassword);
        newPass.setHint(R.string.etNewPassword);
        confirmPass.setHint(R.string.etConfirmNewPassword);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(oldPass);
        linearLayout.addView(newPass);
        linearLayout.addView(confirmPass);
        alertDialog.setView(linearLayout);
        alertDialog.setPositiveButton(R.string.txtChange, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        alertDialog.setNegativeButton(R.string.txtCancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alert = alertDialog.create();
        alert.show();

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    postPassword();
                }
            }
        });
        button = alert.getButton(AlertDialog.BUTTON_POSITIVE);
    }

    private void postPassword() {
        // Reset errors.
        oldPass.setError(null);
        newPass.setError(null);
        confirmPass.setError(null);

        // Store values at the time of the login attempt.
        String oldPassword = oldPass.getText().toString();
        String newPassword = newPass.getText().toString();
        String confirmNewPassword = confirmPass.getText().toString();


        boolean cancel = false;
        View focusView = null;
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            // There are no active networks.
            cancel = true;
            oldPass.setError(context.getString(R.string.errorNoConnection));
        }

        if (TextUtils.isEmpty(newPassword) || !isPasswordValid(newPassword)) {
            newPass.setError(context.getString(R.string.errorNewPassword));
            focusView = newPass;
            cancel = true;
        } else if (TextUtils.isEmpty(confirmNewPassword) || !confirmNewPassword.equals(newPassword)) {
            confirmPass.setError(context.getString(R.string.errorConfirmPassword));
            focusView = confirmPass;
            cancel = true;
        }


        if (TextUtils.isEmpty(oldPassword)) {
            oldPass.setError("Brak hasła");
            focusView = oldPass;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            button.setEnabled(false);
            button.setText("Wysyłanie...");
            try {
                changePassTask = new PasswordTask(oldPassword, newPassword, confirmNewPassword);
                changePassTask.execute();
            } catch (Exception e) {
                e.getMessage();
                button.setEnabled(true);
                button.setText("Zmień");
            }
        }
        return;
    }


    private boolean isPasswordValid(String pass) {
        return pass.length() > 6;
    }


    private class PasswordTask extends AsyncTask<Void, Void, Integer> {

        private final String oldPassword;
        private final String newPassword;
        private final String confirmNewPassword;

        PasswordTask(String oldPassString, String newPassString, String confPassString) {
            oldPassword = oldPassString;
            newPassword = newPassString;
            confirmNewPassword = confPassString;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int code = -1;

            Controller controller = new Controller(new ApiConnection(Settings.BASE_URL));
            try {
                RestResponse response = controller.postChangePassword(oldPassword, newPassword, confirmNewPassword);
                return response.getResponseCode();
            } catch (Exception e) {
                e.getMessage();
                button.setEnabled(true);
                button.setText("Zmień");
            }
            return code;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            if (success == 200) {
                Toast.makeText(context, R.string.toastSuccessChangePassword, Toast.LENGTH_SHORT).show();
                alert.dismiss();
            } else {
                oldPass.setError("Nie udało się zmienić hasła");
                button.setEnabled(true);
                button.setText("Zmień");
            }
        }

        @Override
        protected void onCancelled() {
            oldPass.setError("Nie udało się zmienić hasła");
            button.setText("Zmień");
            button.setEnabled(true);
        }
    }
}
