package com.example.pawfectcareapp.Common;

import android.util.Log;

import com.example.pawfectcareapp.API.ApiCall;
import com.example.pawfectcareapp.BuildConfig;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendEmailFunc {
//    private SweetAlertDialog dialog;
//    public void sendEmail(List<SendEmailModel> list){
//        ApiCall services = ServiceGeneratorForEmail.createService(ApiCall.class, BuildConfig.API_USERNAME, BuildConfig.API_PASSWORD);
//        Call<MawbResponse> call = services.sendEmail(list);
//        SweetAlertDialog finalDialog = dialog;
//        call.enqueue(new Callback<MawbResponse>() {
//            @Override
//            public void onResponse(Call<MawbResponse> call, Response<MawbResponse> response) {
//
//                try {
//
//
//                } catch (Exception e) {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MawbResponse> call, Throwable t) {
//                Log.e("Error:", t.getMessage());
//                System.out.println("Check your connection");
//                Log.e("Error:", t.getMessage());
//            }
//        });
//    }
}
