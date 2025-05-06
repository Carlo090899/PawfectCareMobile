package com.example.pawfectcareapp.ui.Team.viewModel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.pawfectcareapp.API.ApiCall;
import com.example.pawfectcareapp.API.ServiceGenerator;
import com.example.pawfectcareapp.BuildConfig;
import com.example.pawfectcareapp.Common.AlertsAndLoaders;
import com.example.pawfectcareapp.databinding.ActivityDogProfileBinding;
import com.example.pawfectcareapp.databinding.ActivityTeamBinding;
import com.example.pawfectcareapp.ui.Dogs.model.DogProfileResponse;
import com.example.pawfectcareapp.ui.Dogs.view.DogProfile;
import com.example.pawfectcareapp.ui.FeedChart.model.FoodChartResponse;
import com.example.pawfectcareapp.ui.Team.model.TeamModel;
import com.example.pawfectcareapp.ui.Team.model.TeamResponse;
import com.example.pawfectcareapp.ui.Team.view.TeamActivity;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamViewModel {

    TeamResponse resp;
    List<TeamModel> teamList;


    public void getTeam(TeamActivity activity, Context context, ActivityTeamBinding binding, int user_id){
        resp = new TeamResponse();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME, BuildConfig.API_PASS);
        Call<TeamResponse> call = services.getTeamList(user_id);
        call.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                try {
                    teamList = new ArrayList<>();
                    if (response.code() == 200) {
                        resp = response.body();
                        if (resp.getStatusCode() == 200) {

                            binding.teamList.listView.setVisibility(View.VISIBLE);
                            binding.teamList.emptyContainer.setVisibility(View.GONE);

                            teamList = resp.getTeamList();
                            toShowLayout(binding, 1);
                        } else {
                            binding.teamList.listView.setVisibility(View.GONE);
                            binding.teamList.emptyContainer.setVisibility(View.VISIBLE);
                            AlertsAndLoaders alert = new AlertsAndLoaders();
                            alert.showAlert(2, "", resp.getMessage(), context, null);
                        }
                    }

                    activity.getTeamList(teamList);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                Log.e("Error: ", t.getMessage());
            }
        });
    }


    public void updateStatus(TeamActivity activity, Context context, ActivityTeamBinding binding, int id, boolean isActive){
        AlertsAndLoaders alert = new AlertsAndLoaders();
        SweetAlertDialog sDialog = alert.showAlert(3, "Loading . . .", "Updating please wait", context, null);
        ApiCall service = ServiceGenerator.createService(ApiCall.class, "", "");
        Call<TeamResponse> call = service.updateStatus(id,isActive);
        call.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {

                try {
                    resp = new TeamResponse();
                    resp = response.body();
                    if (resp.getStatusCode() == 200) {
                        sDialog.cancel();
                        AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                        alertsAndLoaders.showAlert(0, "", resp.getMessage(), context, activity.backToTeamList);
                    } else {
                        sDialog.cancel();
                        AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                        alertsAndLoaders.showAlert(1, "", resp.getMessage(), context, activity.backToTeamList);
                    }

                } catch (Exception e) {
                    sDialog.cancel();
                    e.printStackTrace();
                    AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                    alertsAndLoaders.showAlert(2, "", e.getMessage(), context, null);
                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                sDialog.cancel();
                AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                alertsAndLoaders.showAlert(2, "", t.getMessage(), context, null);
            }
        });
    }


    public void toShowLayout(ActivityTeamBinding binding, int layout_id) {

        binding.teamList.getRoot().setVisibility(View.GONE);
        binding.headerLayout.getRoot().setVisibility(View.VISIBLE);
        if (layout_id == 0) {

        } else if (layout_id == 1) {
            binding.teamList.getRoot().setVisibility(View.VISIBLE);
        }
    }

}
