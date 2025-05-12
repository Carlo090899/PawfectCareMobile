package com.example.pawfectcareapp.ui.Registration.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.pawfectcareapp.Common.AlertsAndLoaders;
import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.Utils.FunctionInterface;
import com.example.pawfectcareapp.Utils.GenericTextWatcher;
import com.example.pawfectcareapp.databinding.ActivityRegistrationBinding;
import com.example.pawfectcareapp.ui.FeedChart.view.FoodChart;
import com.example.pawfectcareapp.ui.Login.view.LoginPage;
import com.example.pawfectcareapp.ui.MainMenu.MainMenu;
import com.example.pawfectcareapp.ui.Registration.model.RegistrationModel;
import com.example.pawfectcareapp.ui.Registration.viewModel.RegistrationViewModel;

public class Registration extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    RegistrationModel model;
    RegistrationViewModel viewModel;
    String checkedText = "";
    boolean is_clicked = false;
    int layout_id = 1;

//    String [] role = {"Admin", "Employee"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        eventhandler();
    }

    public void init() {
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        model = new RegistrationModel();
        viewModel = new RegistrationViewModel();

        viewModel.toShowLayout(binding, layout_id);

        binding.registrationForm.registrationCard.setBackgroundColor(Color.parseColor("#80FFFFFF"));
        binding.registrationForm.registrationLayout.setBackgroundColor(Color.parseColor("#80FFFFFF"));

//        binding.registrationOtp.otpCard.setBackgroundColor(Color.TRANSPARENT);
//        binding.registrationOtp.otpLayout.setBackgroundColor(Color.parseColor("#80FFFFFF"));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                role
        );
        binding.registrationForm.role.setAdapter(adapter);

        binding.registrationOtp.otp1.addTextChangedListener(new GenericTextWatcher(binding.registrationOtp.otp1, binding.registrationOtp.otp2, null));
        binding.registrationOtp.otp2.addTextChangedListener(new GenericTextWatcher(binding.registrationOtp.otp2, binding.registrationOtp.otp3, binding.registrationOtp.otp1));
        binding.registrationOtp.otp3.addTextChangedListener(new GenericTextWatcher(binding.registrationOtp.otp3, binding.registrationOtp.otp4, binding.registrationOtp.otp2));
        binding.registrationOtp.otp4.addTextChangedListener(new GenericTextWatcher(binding.registrationOtp.otp4, null,binding.registrationOtp.otp3));
    }

    public void eventhandler() {

        binding.registrationForm.sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.registrationForm.fullname.getText().toString().equals("") || !binding.registrationForm.fullname.getText().toString().equals(null) &&
                        !binding.registrationForm.contactNo.getText().toString().equals("") || !binding.registrationForm.contactNo.getText().toString().equals(null) &&
                        !binding.registrationForm.password.getText().toString().equals("") || !binding.registrationForm.contactNo.getText().toString().equals(null) &&
                        !binding.registrationForm.confirmPassword.getText().toString().equals("") || !binding.registrationForm.confirmPassword.getText().toString().equals(null) &&
                        !binding.registrationForm.email.getText().toString().equals("") || !binding.registrationForm.email.getText().toString().equals(null) &&
                        !checkedText.equals("")) {
                    if (binding.registrationForm.password.getText().toString().equals(binding.registrationForm.confirmPassword.getText().toString())) {
                        layout_id = 2;
                        viewModel.toShowLayout(binding, layout_id);
                        model = new RegistrationModel();
                        model.setFullname(binding.registrationForm.fullname.getText().toString());
                        model.setContactNumber(binding.registrationForm.contactNo.getText().toString());
                        model.setPassword(binding.registrationForm.password.getText().toString());
                        model.setConfirm_password(binding.registrationForm.confirmPassword.getText().toString());
                        model.setEmail(binding.registrationForm.email.getText().toString());
                        model.setGender(checkedText);
//                        model.setRole(binding.registrationForm.role.getText().toString());
                        AlertsAndLoaders alert = new AlertsAndLoaders();
                        alert.showAlert(4, "Are you sure you want to proceed?", "This information cannot be undone", Registration.this, saveSignUp);



                    }else{
                        AlertsAndLoaders alert = new AlertsAndLoaders();
                        alert.showAlert(2,"","Password didn't match!", Registration.this, null);
                    }

                }else{
                    AlertsAndLoaders alert = new AlertsAndLoaders();
                    alert.showAlert(2,"","All fields are required!", Registration.this, null);
                }
            }
        });

        binding.registrationForm.genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                for (int i = 0; i < group.getChildCount(); i++) {
                    View child = group.getChildAt(i);
                    if (child instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) child;
                        if (radioButton.isChecked()) {
                            checkedText = radioButton.getText().toString();
                            is_clicked = true;
                            break;
                        }
                    }
                }
            }
        });

        binding.registrationOtp.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpCode = "";
                otpCode = getOTP();
                if(!otpCode .equals("")){
                    viewModel.insertActivationKey(Registration.this,Registration.this, binding, model.getEmail(),Integer.valueOf(otpCode));
                }else{
                    AlertsAndLoaders alert = new AlertsAndLoaders();
                    alert.showAlert(2,"","Please Input OTP", Registration.this, null);
                }
            }
        });

        binding.registrationOtp.Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.resendCode(Registration.this, Registration.this, binding.registrationForm.email.getText().toString());
            }
        });

        binding.registrationOtp.Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_id = 1;
                viewModel.deleteRegistration(binding, layout_id, binding.registrationForm.email.getText().toString());
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        handleBackButton();
    }

    public void handleBackButton() {
        if (layout_id == 1) {
            Intent in = new Intent(Registration.this, MainMenu.class);
            startActivity(in);
        } else {
            layout_id = 1;
            model = new RegistrationModel();
            viewModel.deleteRegistration(binding, layout_id, binding.registrationForm.email.getText().toString());


//            viewModel.toShowLayout(binding, layout_id);
        }

    }

    private String getOTP() {
        return binding.registrationOtp.otp1.getText().toString() +
                binding.registrationOtp.otp2.getText().toString() +
                binding.registrationOtp.otp3.getText().toString() +
                binding.registrationOtp.otp4.getText().toString();
    }

    public FunctionInterface.Function backToLogin = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            Intent in = new Intent(Registration.this, LoginPage.class);
            startActivity(in);
        }
    };

    public FunctionInterface.Function saveSignUp = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            viewModel.saveRegistration(Registration.this, Registration.this, model);
        }
    };

    public FunctionInterface.Function goToActivation = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            layout_id = 2;
            viewModel.toShowLayout(binding,layout_id);
        }
    };

    public FunctionInterface.Function backToRegistrationForm = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            layout_id = 1;

            model = new RegistrationModel();
            binding.registrationForm.fullname.setText("");
            binding.registrationForm.contactNo.setText("");
            binding.registrationForm.password.setText("");
            binding.registrationForm.confirmPassword.setText("");
            binding.registrationForm.email.setText("");
            binding.registrationForm.radioFemale.setChecked(false);
            binding.registrationForm.radioMale.setChecked(false);

            viewModel.toShowLayout(binding,layout_id);

        }
    };



}