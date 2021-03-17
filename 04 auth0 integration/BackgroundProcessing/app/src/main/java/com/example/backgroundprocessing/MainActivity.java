package com.example.backgroundprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    private Auth0 account;
    private CredentialsManager credentialsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        account = new Auth0(this);

        TextView textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.logoutButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

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
                                textView.setText(userProfile.getEmail());
                            }

                            @Override
                            public void onFailure(AuthenticationException e) {
                                e.printStackTrace();
                            }
                        });
            }

            @Override
            public void onFailure(CredentialsManagerException e) {
                e.printStackTrace();
            }
        });
    }

    private void logout() {
        WebAuthProvider
                .logout(account)
                .withScheme(getString(R.string.com_auth0_scheme))
                .start(this, new Callback<Void, AuthenticationException>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        credentialsManager.clearCredentials();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(AuthenticationException e) {
                        e.printStackTrace();
                    }
                });
    }
}