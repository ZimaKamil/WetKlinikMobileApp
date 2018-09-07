package com.example.asztar.wetklinikmobileapp.Activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asztar.wetklinikmobileapp.ApiConn.ApiConnection;
import com.example.asztar.wetklinikmobileapp.ApiConn.RestResponse;
import com.example.asztar.wetklinikmobileapp.ApiConn.Controller;
import com.example.asztar.wetklinikmobileapp.Connectivity;
import com.example.asztar.wetklinikmobileapp.Models.ClientModel;
import com.example.asztar.wetklinikmobileapp.Models.ClinicDb;
import com.example.asztar.wetklinikmobileapp.R;
import com.example.asztar.wetklinikmobileapp.ApiConn.Settings;
import com.example.asztar.wetklinikmobileapp.Token;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.security.ProviderInstaller;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    SharedPreferences preferences;
    private Token token = Token.getInstance();
    private UserLoginTask mAuthTask = null;
    private ClientModel client;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View tvNoConnection;
    private CheckBox cbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        tvNoConnection = findViewById(R.id.tvNoConnection);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        cbRemember = findViewById(R.id.cbRememberPassword);

        ClinicDb adb = ClinicDb.getDatabase(this);
        //getSharedPreferences("user", 0).edit().clear().commit();
        preferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        cbRemember.setChecked(preferences.getBoolean("RememberMe", false));
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        if (getUserAccount(this) != null & cbRemember.isChecked()) {
            mEmailView.setText(getUserAccount(this).name);
            mPasswordView.setText(getUserPassword(this, getUserAccount(this)));
        }
        if (!Connectivity.connectionIsUp(this)) {
            // There are no active networks.
            tvNoConnection.setVisibility(View.VISIBLE);
        }
    }

    private static final String TYPE_ACCOUNT = "klinik.wet";

    public static void addAccount(Context context, String username, String password, String token) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = new Account(username, TYPE_ACCOUNT);
        accountManager.addAccountExplicitly(account, password, null);
        accountManager.setAuthToken(account, TYPE_ACCOUNT, token);
    }

    public static Account getUserAccount(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = null;

        try {
            account = accountManager.getAccountsByType(TYPE_ACCOUNT)[0];
        } catch (Exception ignored) {

        }
        return account;
    }

    public static String getUserPassword(Context context, Account account) {
        AccountManager accountManager = AccountManager.get(context);
        String pass = null;
        try {
            pass = accountManager.getPassword(account);
        } catch (Exception ignored) {
        }
        return pass;
    }

    public void deleteUser(Context context) {
        AccountManager accountManager = AccountManager.get(LoginActivity.this);
        accountManager.removeAccount(accountManager.getAccountsByType("klinik.wet")[0], null, null);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        tvNoConnection.setVisibility(View.GONE);
        if (!Connectivity.connectionIsUp(this)) {
            // There are no active networks.
            cancel = true;
            mEmailView.setError("Brak połączenia z internetem");
            tvNoConnection.setVisibility(View.VISIBLE);
        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            cbRemember.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    cbRemember.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            cbRemember.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                ProviderInstaller.installIfNeeded(getApplicationContext());
                ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                Controller controllerToken = new Controller(new ApiConnection(Settings.BASE_URL));
                RestResponse responseToken = controllerToken.postLogin(mEmail, mPassword);
                if (responseToken.getResponseCode().equals(200)) {
                    token = mapper.readValue(responseToken.getResponse(), Token.class);
                    Controller controllerClient = new Controller(new ApiConnection(Settings.BASE_URL));
                    RestResponse clientResponse = controllerClient.getClient();
                    client = mapper.readValue(clientResponse.getResponse(), ClientModel.class);
                    return clientResponse.getResponseCode();
                }
            } catch (Exception e) {
                e.getMessage();
            }
            return -1;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            mAuthTask = null;
            showProgress(false);

            if (success == 200) {
                preferences.edit().putBoolean("RememberMe", cbRemember.isChecked()).commit();
                preferences = LoginActivity.this.getSharedPreferences(token.getUserName(), Context.MODE_PRIVATE);
                //getSharedPreferences(token.getUserName(), 0).edit().clear().commit();
                preferences.edit().putInt("prefClinic", client.getClinicId()).apply();
                preferences.edit().putString("userName", client.getEmail()).apply();
                Toast.makeText(LoginActivity.this, "Zalogowano jako: " + client.getClientName() + " " + client.getClientSurname(), Toast.LENGTH_SHORT).show();
                //if (getUserAccount(LoginActivity.this) != null)
                    //deleteUser(LoginActivity.this);
                    if (cbRemember.isChecked()) {
                        addAccount(LoginActivity.this, mEmail, mPassword, token.getAccess_token());
                    }
                Intent intent = new Intent(LoginActivity.this, ClinicActivity.class);
                startActivity(intent);
                finish();
            } else {
                mPasswordView.setError("Niepoprawne hasło");
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

