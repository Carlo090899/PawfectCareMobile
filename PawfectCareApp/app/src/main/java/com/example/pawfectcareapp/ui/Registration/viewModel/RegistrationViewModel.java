package com.example.pawfectcareapp.ui.Registration.viewModel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.pawfectcareapp.API.ApiCall;
import com.example.pawfectcareapp.API.ServiceGenerator;
import com.example.pawfectcareapp.BuildConfig;
import com.example.pawfectcareapp.Common.AlertsAndLoaders;
import com.example.pawfectcareapp.Utils.SharedPref;
import com.example.pawfectcareapp.databinding.ActivityRegistrationBinding;
import com.example.pawfectcareapp.ui.Login.model.UserResponse;
import com.example.pawfectcareapp.ui.Registration.model.RegistrationModel;
import com.example.pawfectcareapp.ui.Registration.model.RegistrationResponse;
import com.example.pawfectcareapp.ui.Registration.view.Registration;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationViewModel {

    RegistrationResponse resp;
    private SweetAlertDialog dialog;
    public void saveRegistration(Registration activity, Context context, RegistrationModel model){
        resp = new RegistrationResponse();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME,BuildConfig.API_PASS);
        Call<RegistrationResponse> call = services.saveRegistration(model);
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                try {
                    if (response.code() == 200) {
                        resp = response.body();
                        if (resp.getStatusCode() == 200) {
                            AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                            alertsAndLoaders.showAlert(0, "", "We'll send a 6 digit number on your email.\n Please check it ot verify.", context, activity.goToActivation);
                        }else{
                            AlertsAndLoaders alert =  new AlertsAndLoaders();
                            alert.showAlert(2,"", resp.getMessage(), context, null);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Log.e("Error: ", t.getMessage());
            }
        });

    }

    public void insertActivationKey(Context context, Registration activity, ActivityRegistrationBinding binding, String email, int otpCode){
        resp = new RegistrationResponse();
        AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
        SharedPref util = new SharedPref();

        dialog = alertsAndLoaders.showAlert(3, "Loading...", "", context, null);
        ApiCall services = ServiceGenerator.createService(ApiCall.class, "", "");
        Call<RegistrationResponse> call = services.activateUser(email,otpCode);

        call.enqueue(new Callback<RegistrationResponse>() {

            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {

                try {
                    dialog.cancel();
                    RegistrationResponse res = new RegistrationResponse();
                    res = response.body();
                    if (res.isStatus()) {
                        AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                        alertsAndLoaders.showAlert(0, "", "Your account has been verified successfully.", context, activity.backToLogin);
                    } else {
                        AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                        alertsAndLoaders.showAlert(2, "", res.getMessage(), context, null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                    alertsAndLoaders.showAlert(2, "", e.getMessage(), context, null);
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Log.e("Error:", t.getMessage());

                System.out.println("Check your connection");
                Log.e("Error:", t.getMessage());
                AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                alertsAndLoaders.showAlert(2, "", t.getMessage(), context, null);
            }

        });

    }


    public void toShowLayout(ActivityRegistrationBinding binding, int layout_id) {

        binding.registrationForm.getRoot().setVisibility(View.GONE);
        binding.registrationOtp.getRoot().setVisibility(View.GONE);
        if (layout_id == 1) {
            binding.registrationForm.getRoot().setVisibility(View.VISIBLE);
        } else if (layout_id == 2) {
            binding.registrationOtp.getRoot().setVisibility(View.VISIBLE);
        }
    }

}
