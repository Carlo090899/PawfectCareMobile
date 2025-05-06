package com.example.pawfectcareapp.ui.Login.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pawfectcareapp.Common.AlertsAndLoaders;
import com.example.pawfectcareapp.databinding.ActivityLoginPageBinding;
import com.example.pawfectcareapp.ui.Login.viewModel.LoginViewModel;
import com.example.pawfectcareapp.ui.MainMenu.MainMenu;
import com.example.pawfectcareapp.ui.Registration.view.Registration;

public class LoginPage extends AppCompatActivity {

    ActivityLoginPageBinding binding;
    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        eventHandler();

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void init(){
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new LoginViewModel();
    }

    private void eventHandler(){

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
            @Override
            public void onClick(View view) {

                if(binding.emailAddress.getText().toString() == "" || binding.emailAddress.getText().toString().equals("")) {
                    alertsAndLoaders.showAlert(2, "", "Please enter username", LoginPage.this, null);
                } else if (binding.password.getText().toString() == "" || binding.password.getText().toString().equals("")){
                    alertsAndLoaders.showAlert(2, "", "Please enter password", LoginPage.this, null);
                } else {
                    if(isNetworkConnected()) {
                        viewModel.getUserLogin(binding.emailAddress.getText().toString(), binding.password.getText().toString(), LoginPage.this, LoginPage.this);
                    }else{
                        Toast.makeText(getApplicationContext(), "Please check internet connection!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        binding.newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginPage.this, Registration.class);
                startActivity(in);
            }
        });

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}