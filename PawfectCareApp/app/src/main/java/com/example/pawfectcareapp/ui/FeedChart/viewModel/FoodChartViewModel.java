package com.example.pawfectcareapp.ui.FeedChart.viewModel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.pawfectcareapp.API.ApiCall;
import com.example.pawfectcareapp.API.ServiceGenerator;
import com.example.pawfectcareapp.BuildConfig;
import com.example.pawfectcareapp.Common.AlertsAndLoaders;
import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.Utils.SharedPref;
import com.example.pawfectcareapp.databinding.ActivityDogProfileBinding;
import com.example.pawfectcareapp.databinding.ActivityFeedChartBinding;
import com.example.pawfectcareapp.ui.Dogs.adapter.DogListAdapter;
import com.example.pawfectcareapp.ui.Dogs.model.DogModel;
import com.example.pawfectcareapp.ui.Dogs.model.DogProfileResponse;
import com.example.pawfectcareapp.ui.Dogs.view.DogProfile;
import com.example.pawfectcareapp.ui.FeedChart.model.FoodChartModel;
import com.example.pawfectcareapp.ui.FeedChart.model.FoodChartResponse;
import com.example.pawfectcareapp.ui.FeedChart.view.FoodChart;
import com.example.pawfectcareapp.ui.Task.model.TaskResponse;
import com.example.pawfectcareapp.ui.Task.view.TaskModule;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodChartViewModel {
    private SweetAlertDialog dialog;
    List<DogModel> dogList;
    List<FoodChartModel> foodList;
    DogProfileResponse dogResp;
    FoodChartResponse resp;

    public void getDogList(FoodChart activity, Context context, ActivityFeedChartBinding binding){
        AlertsAndLoaders alert =  new AlertsAndLoaders();
        dialog = alert.showAlert(3,"Please wait", "", context, null);
        dogResp = new DogProfileResponse();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME, BuildConfig.API_PASS);
        Call<DogProfileResponse> call = services.getDogList();
        call.enqueue(new Callback<DogProfileResponse>() {
            @Override
            public void onResponse(Call<DogProfileResponse> call, Response<DogProfileResponse> response) {
                try {
                    dialog.cancel();
                    dogList = new ArrayList<>();
                    if (response.code() == 200) {
                        dogResp = response.body();
                        if (dogResp.getStatusCode() == 200) {

                            binding.dogList.listView.setVisibility(View.VISIBLE);
                            binding.dogList.emptyContainer.setVisibility(View.GONE);

                            dogList = dogResp.getDogs();
                            toShowLayout(binding, 1);
                            viewDogList(context, activity, binding);
                        } else {
                            binding.dogList.listView.setVisibility(View.GONE);
                            binding.dogList.emptyContainer.setVisibility(View.VISIBLE);
                            AlertsAndLoaders alert = new AlertsAndLoaders();
                            alert.showAlert(2, "", dogResp.getMessage(), context, null);
                        }
                    }

                    activity.getDogList(dogList);


                } catch (Exception e) {
                    dialog.cancel();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DogProfileResponse> call, Throwable t) {
                dialog.cancel();
                Log.e("Error: ", t.getMessage());
            }
        });
    }

    public void getFoods(FoodChart activity, Context context, ActivityFeedChartBinding binding, int dog_id){
        AlertsAndLoaders alert =  new AlertsAndLoaders();
        dialog = alert.showAlert(3,"Please wait", "", context, null);
        resp = new FoodChartResponse();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME, BuildConfig.API_PASS);
        Call<FoodChartResponse> call = services.getFoods(dog_id);
        call.enqueue(new Callback<FoodChartResponse>() {
            @Override
            public void onResponse(Call<FoodChartResponse> call, Response<FoodChartResponse> response) {
                try {
                    dialog.cancel();
                    foodList = new ArrayList<>();
                    if (response.code() == 200) {
                        resp = response.body();
                        if (resp.getStatusCode() == 200) {

                            binding.foodChart.listView.setVisibility(View.VISIBLE);
                            binding.foodChart.emptyContainer.setVisibility(View.GONE);

                            foodList = resp.getFoodList();
                        } else {
                            binding.foodChart.listView.setVisibility(View.GONE);
                            binding.foodChart.emptyContainer.setVisibility(View.VISIBLE);

                        }
                    }

                    activity.getFoodList(foodList);


                } catch (Exception e) {
                    dialog.cancel();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FoodChartResponse> call, Throwable t) {
                dialog.cancel();
                Log.e("Error: ", t.getMessage());
            }
        });
    }

    public void saveDogFood(FoodChart activity, Context context, int dog_id, String dog_food, String food_desc, String quantity){
        AlertsAndLoaders alert =  new AlertsAndLoaders();
        dialog = alert.showAlert(3,"Please wait", "", context, null);
        resp = new FoodChartResponse();
        SharedPref util = new SharedPref();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME,BuildConfig.API_PASS);
        Call<FoodChartResponse> call = services.saveDogFood(dog_id, dog_food,food_desc,quantity, Integer.parseInt(util.readPrefString(context, SharedPref.USER_ID)));
        call.enqueue(new Callback<FoodChartResponse>() {
            @Override
            public void onResponse(Call<FoodChartResponse> call, Response<FoodChartResponse> response) {
                try {
                    dialog.cancel();
                    if (response.code() == 200) {
                        resp = response.body();
                        if (resp.getStatusCode() == 200) {
                            AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                            alertsAndLoaders.showAlert(0, "", "Food was successfully added", context, activity.goToFoodChartDetails);
                        }else{
                            AlertsAndLoaders alert =  new AlertsAndLoaders();
                            alert.showAlert(2,"", resp.getMessage(), context, null);
                        }
                    }


                } catch (Exception e) {
                    dialog.cancel();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FoodChartResponse> call, Throwable t) {
                dialog.cancel();
                Log.e("Error: ", t.getMessage());
            }
        });

    }

    public void toShowLayout(ActivityFeedChartBinding binding, int layout_id) {

        binding.dogList.getRoot().setVisibility(View.GONE);
        binding.foodChart.getRoot().setVisibility(View.GONE);
        binding.headerLayout.getRoot().setVisibility(View.VISIBLE);
        if (layout_id == 0) {

        } else if (layout_id == 1) {
            binding.dogList.getRoot().setVisibility(View.VISIBLE);
        } else if (layout_id == 2) {
            binding.foodChart.getRoot().setVisibility(View.VISIBLE);
        }else if(layout_id == 3){

        }
    }

    private void viewDogList(Context context, FoodChart activity, ActivityFeedChartBinding binding) {
        try {
            DogListAdapter adapter = new DogListAdapter(context, activity, R.layout.dog_list_line, dogList);
            binding.dogList.listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFood(Context context, FoodChart activity, int food_id) {
        AlertsAndLoaders alert = new AlertsAndLoaders();
        SweetAlertDialog sDialog = alert.showAlert(3, "Please wait", "", context, null);
        ApiCall service = ServiceGenerator.createService(ApiCall.class, "", "");
        Call<FoodChartResponse> call = service.deleteFood(food_id);
        call.enqueue(new Callback<FoodChartResponse>() {
            @Override
            public void onResponse(Call<FoodChartResponse> call, Response<FoodChartResponse> response) {

                try {
                    resp = new FoodChartResponse();
                    resp = response.body();
                    if (resp.getStatusCode() == 200) {
                        sDialog.cancel();
                        AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                        alertsAndLoaders.showAlert(0, "", resp.getMessage(), context, activity.backToFoodDetails);
                    } else {
                        sDialog.cancel();
                        AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                        alertsAndLoaders.showAlert(1, "", resp.getMessage(), context, activity.backToFoodDetails);
                    }

                } catch (Exception e) {
                    sDialog.cancel();
                    e.printStackTrace();
                    AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                    alertsAndLoaders.showAlert(2, "", e.getMessage(), context, null);
                }
            }

            @Override
            public void onFailure(Call<FoodChartResponse> call, Throwable t) {
                sDialog.cancel();
                AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                alertsAndLoaders.showAlert(2, "", t.getMessage(), context, null);
            }
        });

    }
}
