package com.example.pawfectcareapp.ui.MainMenu.viewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.pawfectcareapp.API.ApiCall;
import com.example.pawfectcareapp.API.ServiceGenerator;
import com.example.pawfectcareapp.BuildConfig;
import com.example.pawfectcareapp.Common.AlertsAndLoaders;
import com.example.pawfectcareapp.Utils.SharedPref;
import com.example.pawfectcareapp.ui.FeedChart.model.FoodChartResponse;
import com.example.pawfectcareapp.ui.Login.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuViewModel {
    UserResponse resp;

    public void hideTermsAndCondition(Context context){
        resp = new UserResponse();
        SharedPref util = new SharedPref();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME,BuildConfig.API_PASS);
        Call<UserResponse> call = services.hideCondition(Integer.valueOf(util.readPrefString(context, util.USER_ID)));
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                try {
                    if (response.code() == 200) {
                        resp = response.body();
                        if (resp.getStatusCode() == 200) {

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
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Error: ", t.getMessage());
            }
        });
    }


}
