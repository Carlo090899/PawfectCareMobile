package com.example.pawfectcareapp.ui.Team.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.pawfectcareapp.Common.AlertsAndLoaders;
import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.Utils.FunctionInterface;
import com.example.pawfectcareapp.Utils.SharedPref;
import com.example.pawfectcareapp.databinding.ActivityDogProfileBinding;
import com.example.pawfectcareapp.databinding.ActivityTeamBinding;
import com.example.pawfectcareapp.ui.Dogs.adapter.DogListAdapter;
import com.example.pawfectcareapp.ui.Dogs.model.DogModel;
import com.example.pawfectcareapp.ui.Dogs.view.DogProfile;
import com.example.pawfectcareapp.ui.FeedChart.view.FoodChart;
import com.example.pawfectcareapp.ui.MainMenu.MainMenu;
import com.example.pawfectcareapp.ui.Task.view.TaskModule;
import com.example.pawfectcareapp.ui.Team.adapter.TeamListAdapter;
import com.example.pawfectcareapp.ui.Team.model.TeamModel;
import com.example.pawfectcareapp.ui.Team.viewModel.TeamViewModel;

import java.util.ArrayList;
import java.util.List;

public class TeamActivity extends AppCompatActivity {

    ActivityTeamBinding binding;
    int layout_id = 1;
    SharedPref util;
    List<TeamModel> teamList, searchTeam, listOfSearchTeam;
    TeamModel selectedEmployee;
    TeamViewModel viewModel;
    AlertDialog dialog = null;
    boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        eventHandler();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        handleBackButton();
    }

    public void handleBackButton() {
        if (layout_id == 1) {
            Intent in = new Intent(TeamActivity.this, MainMenu.class);
            startActivity(in);
        }

    }

    private void init() {
        binding = ActivityTeamBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        util = new SharedPref();
        teamList = new ArrayList<>();
        searchTeam = new ArrayList<>();
        listOfSearchTeam = new ArrayList<>();
        viewModel = new TeamViewModel();

        if(util.readPrefString(TeamActivity.this, util.GENDER).equals("Male")){
            binding.teamList.avatar.setImageResource(R.drawable.man);
        }else{
            binding.teamList.avatar.setImageResource(R.drawable.woman);
        }
        binding.teamList.name.setText(util.readPrefString(TeamActivity.this, util.FULLNAME));
        viewModel.getTeam(TeamActivity.this, TeamActivity.this, binding, Integer.valueOf(util.readPrefString(TeamActivity.this, util.USER_ID)));

        if(Integer.valueOf(util.readPrefString(TeamActivity.this, util.ROLE_ID)) == 2){
            binding.teamList.listView.setEnabled(false);
        }else{
            binding.teamList.listView.setEnabled(true);
        }

    }

    private void eventHandler(){

        binding.teamList.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedEmployee = listOfSearchTeam.get(position);
                if(selectedEmployee.isActive()){
                    AlertsAndLoaders alert = new AlertsAndLoaders();
                    alert.showAlert(4,"Are you sure?", "You want to tag as INACTIVE this user?", TeamActivity.this,updateEmployeeStatus);
                }else{
                    AlertsAndLoaders alert = new AlertsAndLoaders();
                    alert.showAlert(4,"Are you sure?", "You want to tag as ACTIVE this user?", TeamActivity.this,updateEmployeeStatus);
                }
            }
        });

    }

    public void getTeamList(List<TeamModel> teamList) {
        this.teamList = teamList;
        searchTeam = teamList;
        listOfSearchTeam = searchTeam;
        viewTeamList(TeamActivity.this, TeamActivity.this, binding);
    }

    private void viewTeamList(Context context, TeamActivity activity, ActivityTeamBinding binding) {
        try {
            TeamListAdapter adapter = new TeamListAdapter(context, activity, R.layout.team_list_line, searchTeam, Integer.valueOf(util.readPrefString(TeamActivity.this, util.ROLE_ID)));
            binding.teamList.listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FunctionInterface.Function updateEmployeeStatus = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
//            dialog.cancel();
            if(selectedEmployee.isActive()){
                isActive = false;
                viewModel.updateStatus(TeamActivity.this, TeamActivity.this,binding, selectedEmployee.getId(), isActive);
            }else{
                isActive = true;
                viewModel.updateStatus(TeamActivity.this, TeamActivity.this,binding, selectedEmployee.getId(), isActive);
            }

        }
    };

    public FunctionInterface.Function backToTeamList = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            viewModel.getTeam(TeamActivity.this, TeamActivity.this, binding,Integer.valueOf(util.readPrefString(TeamActivity.this, util.USER_ID)));
        }
    };

}