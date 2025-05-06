package com.example.pawfectcareapp.ui.Login.viewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.pawfectcareapp.API.ApiCall;
import com.example.pawfectcareapp.API.ServiceGenerator;
import com.example.pawfectcareapp.BuildConfig;
import com.example.pawfectcareapp.Common.AlertsAndLoaders;
import com.example.pawfectcareapp.Utils.SharedPref;
import com.example.pawfectcareapp.ui.Login.model.UserModel;
import com.example.pawfectcareapp.ui.Login.model.UserResponse;
import com.example.pawfectcareapp.ui.Login.view.LoginPage;
import com.example.pawfectcareapp.ui.MainMenu.MainMenu;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel {
    private SweetAlertDialog dialog;
    private UserModel user;
    private UserResponse resp;

    public void getUserLogin(String username, String password, Context context, LoginPage activity) {
        resp = new UserResponse();

        SharedPref util = new SharedPref();
        AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
        dialog = alertsAndLoaders.showAlert(3, "Logging in, please wait. . .", "", context, null);

        try {
            ApiCall services = ServiceGenerator.createService(ApiCall.class, "", "");
            Call<UserResponse> call = services.getUser(username, password, util.readPrefString(context,BuildConfig.DEVICE_FIREBASE_TOKEN));

            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    dialog.cancel();

                    try {
                        if (response.code() == 200) {
                            resp = response.body();
                            if (resp.getStatusCode() == 200) {
                                user = resp.getData();
                                SharedPref sharedPref = new SharedPref();

                                sharedPref.writePrefString(context, sharedPref.USER_ID, String.valueOf(user.getId()));
                                sharedPref.writePrefString(context, sharedPref.FULLNAME, String.valueOf(user.getFullname()));
                                sharedPref.writePrefString(context, sharedPref.ROLE_ID, String.valueOf(user.getRoleId()));
                                sharedPref.writePrefString(context, sharedPref.GENDER, String.valueOf(user.getGender()));
                                sharedPref.secureWritePrefBoolean(context, String.valueOf(sharedPref.IS_ACTIVE), user.isActive());
                                Intent intent = new Intent(context, MainMenu.class);
                                intent.putExtra("from_login", true);
                                activity.startActivity(intent);

                            } else {
                                alertsAndLoaders.showAlert(1, "", resp.getMessage(), context, null);
                            }
                        } else {
                            alertsAndLoaders.showAlert(1, "", resp.getMessage(), context, null);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Log.e("Error: ", t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
