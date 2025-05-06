package com.example.pawfectcareapp.ui.FeedChart.view;

import static android.view.View.GONE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.pawfectcareapp.BuildConfig;
import com.example.pawfectcareapp.Common.AlertsAndLoaders;
import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.Utils.FunctionInterface;
import com.example.pawfectcareapp.Utils.SharedPref;
import com.example.pawfectcareapp.databinding.ActivityDogProfileBinding;
import com.example.pawfectcareapp.databinding.ActivityFeedChartBinding;
import com.example.pawfectcareapp.ui.Dogs.adapter.AlbumListAdapter;
import com.example.pawfectcareapp.ui.Dogs.adapter.DogListAdapter;
import com.example.pawfectcareapp.ui.Dogs.model.DogAlbumModel;
import com.example.pawfectcareapp.ui.Dogs.model.DogModel;
import com.example.pawfectcareapp.ui.Dogs.view.DogProfile;
import com.example.pawfectcareapp.ui.FeedChart.adapter.FoodListAdapter;
import com.example.pawfectcareapp.ui.FeedChart.model.FoodChartModel;
import com.example.pawfectcareapp.ui.FeedChart.viewModel.FoodChartViewModel;
import com.example.pawfectcareapp.ui.MainMenu.MainMenu;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodChart extends AppCompatActivity {

    ActivityFeedChartBinding binding;
    int layout_id = 1;
    AlertDialog dialog = null;
    FoodChartViewModel viewModel;
    List<DogModel> dogList, searchDog, listOfSearchedDog;
    List<FoodChartModel> foodList;
    SharedPref util;
    DogModel selectedDog = new DogModel();
    TextInputEditText dog_food, food_desc, quantity;
    int food_id = 0;

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
            Intent in = new Intent(FoodChart.this, MainMenu.class);
            startActivity(in);
        } else {
            layout_id--;
            viewModel.toShowLayout(binding, layout_id);
        }

    }

    private void init() {
        binding = ActivityFeedChartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new FoodChartViewModel();
        dogList = new ArrayList<>();
        searchDog = new ArrayList<>();
        listOfSearchedDog = new ArrayList<>();
        foodList = new ArrayList<>();
        util = new SharedPref();

        if (Integer.valueOf(util.readPrefString(FoodChart.this, util.ROLE_ID)) != 1) {
            binding.foodChart.addFood.setVisibility(GONE);
        } else {
            binding.foodChart.addFood.setVisibility(View.VISIBLE);
        }
        binding.dogList.addDog.setVisibility(GONE);
        viewModel.getDogList(FoodChart.this, FoodChart.this, binding);

    }

    private void eventHandler() {

        binding.dogList.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listOfSearchedDog = new ArrayList<>();
                if (newText.equals("") || TextUtils.isEmpty(newText)) {
                    listOfSearchedDog = searchDog;
                } else {
                    for (DogModel c : searchDog) {

                        if ((c.getDogName() != null && c.getDogName().toUpperCase().contains(newText.toUpperCase(Locale.ROOT)))) {
                            listOfSearchedDog.add(c);
                        }

                    }
                }

                viewDogList(FoodChart.this, FoodChart.this, binding);
                return false;
            }
        });

        binding.dogList.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDog = listOfSearchedDog.get(position);

                layout_id = 2;
                String imageUrl = BuildConfig.API_BASE_URL + "dogs/images/" + selectedDog.getFilename();
                Glide.with(FoodChart.this)
                        .load(imageUrl)
                        .into(binding.foodChart.dogImage);

                binding.foodChart.dogName.setText(selectedDog.getDogName().toUpperCase());
                binding.foodChart.birthdate.setText(selectedDog.getBirthdate());
                binding.foodChart.gender.setText(selectedDog.getGender());
                String age = calculateAge(selectedDog.getBirthdate());
                binding.foodChart.age.setText(age);
                if (!selectedDog.getNotes().equals("") || selectedDog.getNotes() != null) {
                    binding.foodChart.notes.setText(selectedDog.getNotes());
                }

                viewModel.toShowLayout(binding, layout_id);
                viewModel.getFoods(FoodChart.this, FoodChart.this, binding, selectedDog.getId());

            }
        });

        binding.foodChart.addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodDialog();
            }
        });

    }

    public void getDogList(List<DogModel> dogList) {
        this.dogList = dogList;
        searchDog = dogList;
        listOfSearchedDog = searchDog;
    }

    public void deleteFood(int food_id) {
        this.food_id = food_id;
        AlertsAndLoaders alert = new AlertsAndLoaders();
        alert.showAlert(4, "Are you sure?", "You want to delete this food from the list?", FoodChart.this, deleteFood);
    }


    private void viewDogList(Context context, FoodChart activity, ActivityFeedChartBinding binding) {
        try {
            DogListAdapter adapter = new DogListAdapter(context, activity, R.layout.dog_list_line, listOfSearchedDog);
            binding.dogList.listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String calculateAge(String birthdate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate birthDate = LocalDate.parse(birthdate, formatter);
            LocalDate today = LocalDate.now();

            Period age = Period.between(birthDate, today);
            int years = age.getYears();
            int months = age.getMonths();

            StringBuilder ageText = new StringBuilder();
            if (years > 0) {
                ageText.append(years).append(" yr");
                if (years > 1) ageText.append("s");
            }
            if (months > 0) {
                if (years > 0) ageText.append(" ");
                ageText.append(months).append(" mo");
                if (months > 1) ageText.append("s");
            }

            return ageText.length() > 0 ? ageText + " old" : "0 month old";

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    @SuppressLint("MissingInflatedId")
    public void showAddFoodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FoodChart.this);
        View mview = getLayoutInflater().inflate(R.layout.add_dog_food_dialog, null);
        CardView btn_cancel;
        LinearLayout save_food;

        dog_food = mview.findViewById(R.id.dog_food);
        food_desc = mview.findViewById(R.id.food_desc);
        quantity = mview.findViewById(R.id.quantity);
        btn_cancel = mview.findViewById(R.id.btn_cancel);
        save_food = mview.findViewById(R.id.save_food);


        save_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();

                if (!dog_food.getText().toString().equals("") || !food_desc.getText().toString().equals("") || !quantity.getText().toString().equals("")) {
                    alertsAndLoaders.showAlert(4, "Are you sure?", "You want to add this food to your list?", FoodChart.this, saveFood);
                } else {
                    alertsAndLoaders.showAlert(2, "", "All Fields are Required", FoodChart.this, null);
                }

            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        builder.setView(mview);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public FunctionInterface.Function saveFood = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            viewModel.saveDogFood(FoodChart.this, FoodChart.this, selectedDog.getId(), dog_food.getText().toString(), food_desc.getText().toString(), quantity.getText().toString());
        }
    };

    public FunctionInterface.Function goToFoodChartDetails = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            dialog.cancel();
            viewModel.getFoods(FoodChart.this, FoodChart.this, binding, selectedDog.getId());
        }
    };

    public FunctionInterface.Function backToFoodDetails = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            viewModel.getFoods(FoodChart.this, FoodChart.this, binding, selectedDog.getId());
        }
    };

    public FunctionInterface.Function deleteFood = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            viewModel.deleteFood(FoodChart.this, FoodChart.this, food_id);
        }
    };

    public void getFoodList(List<FoodChartModel> foodList) {
        this.foodList = foodList;
        viewAlbums(FoodChart.this, binding);
    }


    private void viewAlbums(Context context, ActivityFeedChartBinding binding) {
        try {
            binding.foodChart.listView.setLayoutManager(new LinearLayoutManager(FoodChart.this, LinearLayoutManager.VERTICAL, false));

            FoodListAdapter adapter = new FoodListAdapter(FoodChart.this, R.layout.food_list_line, foodList, binding, FoodChart.this, Integer.valueOf(util.readPrefString(FoodChart.this, util.ROLE_ID)));
            binding.foodChart.listView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}