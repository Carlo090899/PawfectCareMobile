package com.example.pawfectcareapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.pawfectcareapp.Utils.SharedPref;
import com.example.pawfectcareapp.ui.Login.view.LoginPage;
import com.example.pawfectcareapp.ui.MainMenu.MainMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        System.out.println("My token>>>>>>>>>>>"+token);
                        SharedPref util=new SharedPref();
                        util.writePrefString(MainActivity.this,BuildConfig.DEVICE_FIREBASE_TOKEN, token);

                    }
                });

        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(2500);
                    startActivity(new Intent(MainActivity.this, LoginPage.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();

    }
}