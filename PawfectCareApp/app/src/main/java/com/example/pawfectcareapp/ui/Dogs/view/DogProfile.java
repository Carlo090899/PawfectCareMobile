package com.example.pawfectcareapp.ui.Dogs.view;

import static android.view.View.GONE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pawfectcareapp.BuildConfig;
import com.example.pawfectcareapp.Common.AlertsAndLoaders;
import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.Utils.FunctionInterface;
import com.example.pawfectcareapp.Utils.SharedPref;
import com.example.pawfectcareapp.databinding.ActivityDogProfileBinding;
import com.example.pawfectcareapp.databinding.ActivityTaskBinding;
import com.example.pawfectcareapp.ui.Dogs.adapter.AlbumImageListAdapter;
import com.example.pawfectcareapp.ui.Dogs.adapter.AlbumListAdapter;
import com.example.pawfectcareapp.ui.Dogs.adapter.DogListAdapter;
import com.example.pawfectcareapp.ui.Dogs.model.AlbumImagesModel;
import com.example.pawfectcareapp.ui.Dogs.model.DogAlbumModel;
import com.example.pawfectcareapp.ui.Dogs.model.DogModel;
import com.example.pawfectcareapp.ui.Dogs.viewModel.DogProfileViewModel;
import com.example.pawfectcareapp.ui.MainMenu.MainMenu;
import com.example.pawfectcareapp.ui.Task.model.TaskModel;
import com.example.pawfectcareapp.ui.Task.view.TaskModule;
import com.example.pawfectcareapp.ui.Task.viewModel.TaskViewModel;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DogProfile extends AppCompatActivity {

    ActivityDogProfileBinding binding;
    AlertDialog dialog = null;
    ImageView picture, album_pic;
    TextInputEditText birthdate, editBirthdate;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri imageUri;
    private List<Uri> uris;
    private byte[] bytes;
    int layout_id = 1;
    TextInputEditText album_name;
    List<String> genderList = Arrays.asList("Male", "Female");
    private static String select_b64 = "";
    boolean is_pic_taken = false;

    DogModel dogModel = new DogModel();

    DogProfileViewModel viewModel;

    List<DogModel> dogList, searchDog, listOfSearchedDog;
    List<AlbumImagesModel> albumImageList;
    List<DogAlbumModel> dogAlbumList;
    DogAlbumModel selectedAlbum = new DogAlbumModel();
    DogModel selectedDog = new DogModel();
    SharedPref util;
    boolean for_album = false;
    int album_id = 0;
    boolean is_edit = false;
    boolean is_edit_album = false;

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
            Intent in = new Intent(DogProfile.this, MainMenu.class);
            startActivity(in);
        } else {
            layout_id--;
            viewModel.toShowLayout(binding, layout_id);
        }

    }

    private void init() {
        binding = ActivityDogProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new DogProfileViewModel();
        dogList = new ArrayList<>();
        searchDog = new ArrayList<>();
        listOfSearchedDog = new ArrayList<>();
        dogAlbumList = new ArrayList<>();
        albumImageList = new ArrayList<>();
        util = new SharedPref();

        if (Integer.valueOf(util.readPrefString(DogProfile.this, util.ROLE_ID)) != 1) {
            binding.dogList.addDog.setVisibility(GONE);
            binding.dogDetails.addAlbum.setVisibility(GONE);
            binding.albumContent.captureImage.setVisibility(GONE);
            binding.dogDetails.editDetails.setVisibility(GONE);
        } else {
            binding.dogList.addDog.setVisibility(View.VISIBLE);
            binding.dogDetails.addAlbum.setVisibility(View.VISIBLE);
            binding.albumContent.captureImage.setVisibility(View.VISIBLE);
            binding.dogDetails.editDetails.setVisibility(View.VISIBLE);
        }

        viewModel.getDogList(DogProfile.this, DogProfile.this, binding);
        binding.headerLayout.notificationIcon.setVisibility(View.GONE);

    }

    private void eventHandler() {

        binding.dogList.addDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDogDialog();
            }
        });

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

                viewDogList(DogProfile.this, DogProfile.this, binding);
                return false;
            }
        });

        binding.dogList.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDog = listOfSearchedDog.get(position);

                layout_id = 2;
                String imageUrl = BuildConfig.API_BASE_URL + "dogs/images/" + selectedDog.getFilename();
                Glide.with(DogProfile.this)
                        .load(imageUrl)
                        .into(binding.dogDetails.dogImage);

                binding.dogDetails.dogName.setText(selectedDog.getDogName().toUpperCase());
                binding.dogDetails.birthdate.setText(selectedDog.getBirthdate());
                binding.dogDetails.gender.setText(selectedDog.getGender());
                String age = calculateAge(selectedDog.getBirthdate());
                binding.dogDetails.age.setText(age);
                if (!selectedDog.getNotes().equals("") || selectedDog.getNotes() != null) {
                    binding.dogDetails.notes.setText(selectedDog.getNotes());
                }

                viewModel.toShowLayout(binding, layout_id);
                viewModel.getAlbum(DogProfile.this, DogProfile.this, binding, selectedDog.getId());
            }

        });

        binding.dogDetails.addAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAlbumDialog();
            }
        });

        binding.albumContent.captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for_album = true;
                showCaptureAlbum();
            }
        });

        binding.dogDetails.editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDogDetailsDialog();
            }
        });

        binding.albumContent.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_edit_album = false;
                showAddAlbumDialog();
            }
        });

        binding.albumContent.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertsAndLoaders alert = new AlertsAndLoaders();
                alert.showAlert(4, "Are you sure?","You want to delete this album?", DogProfile.this,deleteAlbum);
            }
        });

    }

    public void showEditDogDetailsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DogProfile.this);
        View mview = getLayoutInflater().inflate(R.layout.edit_dog_dialog, null);

        TextInputEditText dog_name, notes;
        CardView btn_cancel;
        LinearLayout save_details;
        ImageView datePicker;

        dog_name = mview.findViewById(R.id.edit_dog_name);
        editBirthdate = mview.findViewById(R.id.edit_birthdate);
        datePicker = mview.findViewById(R.id.date_picker);
        notes = mview.findViewById(R.id.edit_notes);

        btn_cancel = mview.findViewById(R.id.btn_cancel);
        save_details = mview.findViewById(R.id.save_details);

        dog_name.setText(selectedDog.getDogName());
        editBirthdate.setText(selectedDog.getBirthdate());
        notes.setText(selectedDog.getNotes());

        is_edit = true;
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });


        save_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                    if (!dog_name.getText().toString().trim().equals("") && !birthdate.getText().toString().trim().equals("")) {

                        dogModel.setDogName(dog_name.getText().toString().trim());
                        dogModel.setBirthdate(editBirthdate.getText().toString().trim());
                        dogModel.setNotes(notes.getText().toString().trim());

                        viewModel.editDogDetails(DogProfile.this, DogProfile.this, dogModel, selectedDog.getId());

                        dialog.cancel();

                    } else {
                        alertsAndLoaders.showAlert(2, "", "Please Insert dog name and birthdate", DogProfile.this, null);
                    }


            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_edit = false;
                dialog.cancel();
            }
        });

        builder.setView(mview);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showCaptureAlbum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DogProfile.this);
        View mview = getLayoutInflater().inflate(R.layout.capture_album_image, null);
        CardView btn_cancel;
        LinearLayout save_image;

        btn_cancel = mview.findViewById(R.id.btn_cancel);
        save_image = mview.findViewById(R.id.save_image);
        album_pic = mview.findViewById(R.id.picture);


        album_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });

        save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    AlertsAndLoaders alert = new AlertsAndLoaders();
                    alert.showAlert(4, "Are you sure?", "You want to save this image in this album?", DogProfile.this, saveAlbumImage);
                } else {
                    AlertsAndLoaders alert = new AlertsAndLoaders();
                    alert.showAlert(2, "", "Please Capture an Image first!", DogProfile.this, null);
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

    @SuppressLint("MissingInflatedId")
    public void showAddAlbumDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DogProfile.this);
        View mview = getLayoutInflater().inflate(R.layout.add_album_dialog, null);
        CardView btn_cancel;
        LinearLayout save_album;

        album_name = mview.findViewById(R.id.album_name);
        btn_cancel = mview.findViewById(R.id.btn_cancel);
        save_album = mview.findViewById(R.id.save_album);

        if(is_edit_album){
            album_name.setText(selectedAlbum.getAlbumName());
        }

        save_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();

                if(is_edit_album){
                    if (!album_name.getText().toString().equals("")) {
                        alertsAndLoaders.showAlert(4, "Are you sure?", "You want to change the name of this album?", DogProfile.this, saveAlbum);
                    } else {
                        alertsAndLoaders.showAlert(2, "", "Please insert album name to proceed", DogProfile.this, null);
                    }
                }else{
                    if (!album_name.getText().toString().equals("")) {
                        alertsAndLoaders.showAlert(4, "Are you sure?", "You want to save this album to your list?", DogProfile.this, saveAlbum);
                    } else {
                        alertsAndLoaders.showAlert(2, "", "Please insert album name to proceed", DogProfile.this, null);
                    }
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

    public void showAddDogDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DogProfile.this);
        View mview = getLayoutInflater().inflate(R.layout.add_dog_dialog, null);

        TextInputEditText dog_name, notes;
        AutoCompleteTextView gender;
        CardView btn_cancel;
        LinearLayout save_details;
        ImageView datePicker;

        gender = mview.findViewById(R.id.gender);
        dog_name = mview.findViewById(R.id.dog_name);
        birthdate = mview.findViewById(R.id.birthdate);
        datePicker = mview.findViewById(R.id.date_picker);
        notes = mview.findViewById(R.id.notes);
        picture = mview.findViewById(R.id.picture);

        btn_cancel = mview.findViewById(R.id.btn_cancel);
        save_details = mview.findViewById(R.id.save_details);


//        -- SET GENDER IN DROP DOWN
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                DogProfile.this,
                android.R.layout.simple_dropdown_item_1line,
                genderList
        );

        gender.setAdapter(adapter);


        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });


        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();

            }
        });


        save_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                if (is_pic_taken) {
                    if (!dog_name.getText().toString().trim().equals("") && !birthdate.getText().toString().trim().equals("") && !gender.getText().toString().trim().equals("")) {

                        dogModel.setDogName(dog_name.getText().toString().trim());
                        dogModel.setBirthdate(birthdate.getText().toString().trim());
                        dogModel.setGender(gender.getText().toString().trim());
                        dogModel.setNotes(notes.getText().toString().trim());
                        dogModel.setUri(imageUri);
                        uris = new ArrayList<>();
                        uris.add(imageUri);

                        viewModel.saveDogDetails(DogProfile.this, DogProfile.this, dogModel, uris);

                        is_pic_taken = false;
                        dialog.cancel();

                    } else {
                        alertsAndLoaders.showAlert(2, "", "Please Insert dog name, gender and birthdate", DogProfile.this, null);
                    }
                } else {
                    alertsAndLoaders.showAlert(2, "", "Please take an image...", DogProfile.this, null);
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

    private void askCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            // Create a file to save the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Handle error
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", photoFile);
                if (imageUri != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }

        }

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ROOT).format(new Date());

        String imageFileName = timeStamp;

        File storageDir = getCacheDir();
        File image = File.createTempFile(imageFileName, ".png", storageDir);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            int newWidth = 700; // Replace with your desired width
            int newHeight = 1000;
            Bitmap imageBitmap = null;

            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, newWidth, newHeight, true);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            bytes = stream.toByteArray();

            if (!for_album) {
                is_pic_taken = true;
                picture.setImageURI(imageUri);
                picture.setBackground(null);
            } else {
                album_pic.setImageURI(imageUri);
                album_pic.setBackground(null);
            }


            select_b64 = Base64.encodeToString(bytes, Base64.DEFAULT);

        }
    }

    private void showDatePicker() {
        final Calendar currentDate = Calendar.getInstance();

        new DatePickerDialog(DogProfile.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                if (is_edit) {
                    editBirthdate.setText(dateFormat.format(selectedDate.getTime()));
                } else {
                    birthdate.setText(dateFormat.format(selectedDate.getTime()));
                }
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public FunctionInterface.Function saveAlbumImage = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            dialog.cancel();
            uris = new ArrayList<>();
            uris.add(imageUri);
            viewModel.saveAlbumImage(DogProfile.this, DogProfile.this, uris, album_id);
        }
    };

    public FunctionInterface.Function goToDogList = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            dialog.cancel();
            viewModel.getDogList(DogProfile.this, DogProfile.this, binding);
        }
    };

    public FunctionInterface.Function goToAlbumList = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            dialog.cancel();
            layout_id = 2;
            viewModel.getAlbum(DogProfile.this, DogProfile.this, binding, selectedDog.getId());
            viewModel.toShowLayout(binding, layout_id);
        }
    };

    public FunctionInterface.Function goToDogDetails = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            is_edit_album = false;
            is_edit = false;
            dialog.cancel();
            viewModel.getAlbum(DogProfile.this, DogProfile.this, binding, selectedDog.getId());
        }
    };

    public FunctionInterface.Function saveAlbum = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            if(is_edit_album){
                viewModel.editAlbumName(DogProfile.this, DogProfile.this, selectedAlbum.getId(), album_name.getText().toString());
            }else{
                viewModel.saveAlbumName(DogProfile.this, DogProfile.this, selectedDog.getId(), album_name.getText().toString());
            }

        }
    };

    public FunctionInterface.Function deleteAlbum = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            viewModel.deleteAlbum(DogProfile.this, DogProfile.this,  selectedAlbum.getId());
        }
    };

    public void goToAlbumContent(int dog_album_id) {
        layout_id = 3;
        album_id = dog_album_id;
        viewModel.toShowLayout(binding, layout_id);
        viewModel.getImageList(DogProfile.this, DogProfile.this, binding, album_id);
    }

    public void getDogList(List<DogModel> dogList) {
        this.dogList = dogList;
        searchDog = dogList;
        listOfSearchedDog = searchDog;
    }

    public void getAlbumImageList(List<AlbumImagesModel> albumImageList) {
        this.albumImageList = albumImageList;
        System.out.println("albumImageList.size()>>>>>>>>>>>>" + albumImageList.size());
        viewAlbumImageList(DogProfile.this, DogProfile.this, binding);
    }

    public void getAlbumList(List<DogAlbumModel> dogAlbumList) {
        this.dogAlbumList = dogAlbumList;
        viewAlbums(DogProfile.this, binding);
    }

    private void viewDogList(Context context, DogProfile activity, ActivityDogProfileBinding binding) {
        try {
            DogListAdapter adapter = new DogListAdapter(context, activity, R.layout.dog_list_line, listOfSearchedDog);
            binding.dogList.listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewAlbums(Context context, ActivityDogProfileBinding binding) {
        try {
            binding.dogDetails.listView.setLayoutManager(new LinearLayoutManager(DogProfile.this, LinearLayoutManager.HORIZONTAL, false));

            AlbumListAdapter adapter = new AlbumListAdapter(DogProfile.this, R.layout.album_list_line, dogAlbumList, binding, DogProfile.this);
            binding.dogDetails.listView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewAlbumImageList(Context context, DogProfile activity, ActivityDogProfileBinding binding) {
        try {

            binding.albumContent.listView.setLayoutManager(new LinearLayoutManager(DogProfile.this, LinearLayoutManager.HORIZONTAL, false));
            binding.albumContent.listView.setHasFixedSize(true);
            AlbumImageListAdapter adapter = new AlbumImageListAdapter(DogProfile.this, R.layout.album_image_list_line, albumImageList, binding, DogProfile.this);
            binding.albumContent.listView.setAdapter(adapter);
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

    public void showImageInFullScreen(String imageUrl) {
        if (imageUrl != null) {
            layout_id = 4;
            viewModel.toShowLayout(binding, layout_id);
            Glide.with(this).load(imageUrl).into(binding.activityFullScreenImage.fullScreenImage);
        }
    }


}