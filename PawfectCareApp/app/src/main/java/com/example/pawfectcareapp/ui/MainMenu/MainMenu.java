package com.example.pawfectcareapp.ui.MainMenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;

import com.example.pawfectcareapp.BuildConfig;
import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.Utils.SharedPref;
import com.example.pawfectcareapp.databinding.ActivityMainMenuBinding;
import com.example.pawfectcareapp.ui.Dogs.view.DogProfile;
import com.example.pawfectcareapp.ui.FeedChart.view.FoodChart;
import com.example.pawfectcareapp.ui.Login.view.LoginPage;
import com.example.pawfectcareapp.ui.MainMenu.viewModel.MainMenuViewModel;
import com.example.pawfectcareapp.ui.Task.view.TaskModule;
import com.example.pawfectcareapp.ui.Team.view.TeamActivity;
import com.example.pawfectcareapp.ui.notificatioHelper.MyFirebaseMessagingService;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainMenu extends AppCompatActivity {

    ActivityMainMenuBinding binding;
    AlertDialog dialog = null;
    CheckBox checkBox;
    boolean from_login = false;
    SharedPref utils;
    MainMenuViewModel viewModel;

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

        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(MainMenu.this).setMessage("Are you sure you want to Logout?").setPositiveButton("Yes", null).setNegativeButton("Cancel", null).show();
        Button positiveButton = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent in = new Intent(MainMenu.this, LoginPage.class);
                    startActivity(in);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }


    private void init() {
        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        utils = new SharedPref();
        viewModel = new MainMenuViewModel();

        startTimer();

        try {
            if (!utils.secureReadPrefBoolean(MainMenu.this, String.valueOf(utils.IS_READ_TC))) {
                showTermsAndConditions();
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            showTermsAndConditions(); // or handle it appropriately
        }
    }

    public void eventHandler() {

        binding.task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                if(Integer.valueOf(utils.readPrefString(MainMenu.this, SharedPref.ROLE_ID)) == 1){
                    utils.writePrefString(MainMenu.this, BuildConfig.HAS_NOTIFICATION_COMPLETE_TASK, "false");
                }else{
                    utils.writePrefString(MainMenu.this, BuildConfig.HAS_NOTIFICATION_ASSIGN_TASK, "false");
                }


                Intent in = new Intent(MainMenu.this, TaskModule.class);
                startActivity(in);
            }
        });

        binding.doggos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                Intent in = new Intent(MainMenu.this, DogProfile.class);
                startActivity(in);
            }
        });

        binding.foodChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                Intent in = new Intent(MainMenu.this, FoodChart.class);
                startActivity(in);
            }
        });

        binding.team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                Intent in = new Intent(MainMenu.this, TeamActivity.class);
                startActivity(in);
            }
        });

    }

    @SuppressLint("MissingInflatedId")
    public void showTermsAndConditions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
        View mview = getLayoutInflater().inflate(R.layout.terms_condition_dialog, null);
        ScrollView scrollView;
        Button btnConfirm;
        checkBox = mview.findViewById(R.id.checkbox_agree);
        btnConfirm = mview.findViewById(R.id.btn_confirm);
        scrollView = mview.findViewById(R.id.scroll_view);

        // Initially disable the checkbox and the button
        checkBox.setEnabled(false);
        btnConfirm.setEnabled(false);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (!scrollView.canScrollVertically(1)) {
                    checkBox.setEnabled(true);
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnConfirm.setEnabled(true);
                } else {
                    btnConfirm.setEnabled(false);
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                viewModel.hideTermsAndCondition(MainMenu.this);
            }
        });

        builder.setView(mview);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnKeyListener((dialogInterface, keyCode, event) -> {
            return keyCode == KeyEvent.KEYCODE_BACK;
        });

        dialog.show();
    }

    public void startTimer() {
        runnable.run();
    }

    public void stopTimer() {
        handler.removeCallbacks(runnable);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            afficher();
        }
    };

    public void afficher() {
        showNotification();
        handler.postDelayed(runnable, 1000);
    }

    public void showNotification() {

        if (utils.readPrefString(MainMenu.this, BuildConfig.HAS_NOTIFICATION_ASSIGN_TASK) == null || utils.readPrefString(MainMenu.this, BuildConfig.HAS_NOTIFICATION_ASSIGN_TASK).equals("false")) {
            binding.notifTask.setVisibility(View.GONE);
        } else if (utils.readPrefString(MainMenu.this, BuildConfig.HAS_NOTIFICATION_ASSIGN_TASK).equals("true")) {
            if (Integer.valueOf(utils.readPrefString(MainMenu.this, SharedPref.ROLE_ID)) == 2) {
                String notifState = utils.readPrefString(MainMenu.this, BuildConfig.HAS_NOTIFICATION_ASSIGN_TASK);
                Log.d("NotifCheck1", "HAS_NOTIFICATION_ASSIGN_TASK = " + notifState);
                binding.notifTask.setVisibility(View.VISIBLE);
                binding.notifTask.setAnimation(AnimationUtils.loadAnimation(MainMenu.this, R.anim.shake_animation));
            }
        }

        if (utils.readPrefString(MainMenu.this, BuildConfig.HAS_NOTIFICATION_COMPLETE_TASK) == null || utils.readPrefString(MainMenu.this, BuildConfig.HAS_NOTIFICATION_COMPLETE_TASK).equals("false")) {
            binding.notifTask.setVisibility(View.GONE);
        } else if (utils.readPrefString(MainMenu.this, BuildConfig.HAS_NOTIFICATION_COMPLETE_TASK).equals("true")) {
            if (Integer.valueOf(utils.readPrefString(MainMenu.this, SharedPref.ROLE_ID)) == 1) {
                String notifState = utils.readPrefString(MainMenu.this, BuildConfig.HAS_NOTIFICATION_COMPLETE_TASK);
                Log.d("NotifCheck2", "HAS_NOTIFICATION_COMPLETE_TASK = " + notifState);
                binding.notifTask.setVisibility(View.VISIBLE);
                binding.notifTask.setAnimation(AnimationUtils.loadAnimation(MainMenu.this, R.anim.shake_animation));
            }
        }


    }

}