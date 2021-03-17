package com.example.backgroundprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.authentication.storage.CredentialsManager;
import com.auth0.android.authentication.storage.CredentialsManagerException;
import com.auth0.android.authentication.storage.SharedPreferencesStorage;
import com.auth0.android.callback.Callback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;

public class LoginActivity extends AppCompatActivity {

    private Auth0 account;
    private CredentialsManager credentialsManager;
    private Button button;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        account = new Auth0(this);

        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AuthenticationAPIClient authAPIClient = new AuthenticationAPIClient(account);
        SharedPreferencesStorage sharedPrefStorage = new SharedPreferencesStorage(this);
        credentialsManager = new CredentialsManager(authAPIClient, sharedPrefStorage);

        credentialsManager.getCredentials(new Callback<Credentials, CredentialsManagerException>() {
            @Override
            public void onSuccess(Credentials credentials) {
                authAPIClient.userInfo(credentials.getAccessToken())
                        .start(new Callback<UserProfile, AuthenticationException>() {
                            @Override
                            public void onSuccess(UserProfile userProfile) {
                                showToastText("Automatic Login Success");
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }

                            @Override
                            public void onFailure(AuthenticationException e) {
                                showToastText("Session Expired, please Log In");
                                credentialsManager.clearCredentials();
                                showLoginButton();
                            }
                        });
            }

            @Override
            public void onFailure(CredentialsManagerException e) {
                e.printStackTrace();
                showLoginButton();

            }
        });
    }

    private void login() {
        WebAuthProvider
                .login(account)
                .withScheme(getString(R.string.com_auth0_scheme))
                .start(this, new Callback<Credentials, AuthenticationException>() {
                    @Override
                    public void onSuccess(Credentials credentials) {
                        showToastText("Log In - Success");
                        credentialsManager.saveCredentials(credentials);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(AuthenticationException e) {
                        showToastText("Log In - Failure");
                        e.printStackTrace();
                    }
                });
    }

    private void showToastText(final String text) {
        Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void showLoginButton() {
        progressBar.setVisibility(ProgressBar.GONE);
        button.setVisibility(Button.VISIBLE);
    }
}