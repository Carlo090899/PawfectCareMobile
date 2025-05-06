package com.example.pawfectcareapp.ui.Dogs.viewModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pawfectcareapp.API.ApiCall;
import com.example.pawfectcareapp.API.ServiceGenerator;
import com.example.pawfectcareapp.BuildConfig;
import com.example.pawfectcareapp.Common.AlertsAndLoaders;
import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.Utils.RotateImage;
import com.example.pawfectcareapp.Utils.SharedPref;
import com.example.pawfectcareapp.databinding.ActivityDogProfileBinding;
import com.example.pawfectcareapp.ui.Dogs.adapter.AlbumListAdapter;
import com.example.pawfectcareapp.ui.Dogs.adapter.DogListAdapter;
import com.example.pawfectcareapp.ui.Dogs.model.AlbumImagesModel;
import com.example.pawfectcareapp.ui.Dogs.model.DogAlbumModel;
import com.example.pawfectcareapp.ui.Dogs.model.DogModel;
import com.example.pawfectcareapp.ui.Dogs.model.DogProfileResponse;
import com.example.pawfectcareapp.ui.Dogs.view.DogProfile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DogProfileViewModel {

    DogProfileResponse resp;
    private SweetAlertDialog dialog;
    List<DogModel> dogList;
    List<AlbumImagesModel> imageList;
    List<DogAlbumModel> albumList;
    public void saveDogDetails(DogProfile activity, Context context, DogModel model, List<Uri> uri){
        resp = new DogProfileResponse();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME,BuildConfig.API_PASS);
        Call<DogProfileResponse> call = services.saveDogDetails(getFilePart(uri, context), model.getDogName(), model.getBirthdate(), model.getGender(), model.getNotes());
        call.enqueue(new Callback<DogProfileResponse>() {
            @Override
            public void onResponse(Call<DogProfileResponse> call, Response<DogProfileResponse> response) {
                try {
                    if (response.code() == 200) {
                        resp = response.body();
                        if (resp.getStatusCode() == 200) {
                            AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                            alertsAndLoaders.showAlert(0, "", "Dog was Successfully Registered", context, activity.goToDogList);
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
            public void onFailure(Call<DogProfileResponse> call, Throwable t) {
                Log.e("Error: ", t.getMessage());
            }
        });

    }

    public void getDogList(DogProfile activity, Context context, ActivityDogProfileBinding binding){
        resp = new DogProfileResponse();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME, BuildConfig.API_PASS);
        Call<DogProfileResponse> call = services.getDogList();
        call.enqueue(new Callback<DogProfileResponse>() {
            @Override
            public void onResponse(Call<DogProfileResponse> call, Response<DogProfileResponse> response) {
                try {
                    dogList = new ArrayList<>();
                    if (response.code() == 200) {
                        resp = response.body();
                        if (resp.getStatusCode() == 200) {

                            binding.dogList.listView.setVisibility(View.VISIBLE);
                            binding.dogList.emptyContainer.setVisibility(View.GONE);

                            dogList = resp.getDogs();
                            toShowLayout(binding, 1);
                            viewDogList(context, activity, binding);
                        } else {
                            binding.dogList.listView.setVisibility(View.GONE);
                            binding.dogList.emptyContainer.setVisibility(View.VISIBLE);
                            AlertsAndLoaders alert = new AlertsAndLoaders();
                            alert.showAlert(2, "", resp.getMessage(), context, null);
                        }
                    }

                    activity.getDogList(dogList);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DogProfileResponse> call, Throwable t) {
                Log.e("Error: ", t.getMessage());
            }
        });
    }

    public void saveAlbumName(DogProfile activity, Context context, int dog_id, String album_name){
        resp = new DogProfileResponse();
        SharedPref util = new SharedPref();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME,BuildConfig.API_PASS);
        Call<DogProfileResponse> call = services.saveAlbumName(dog_id, album_name, Integer.valueOf(util.readPrefString(context, util.USER_ID)));
        call.enqueue(new Callback<DogProfileResponse>() {
            @Override
            public void onResponse(Call<DogProfileResponse> call, Response<DogProfileResponse> response) {
                try {
                    if (response.code() == 200) {
                        resp = response.body();
                        if (resp.getStatusCode() == 200) {
                            AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                            alertsAndLoaders.showAlert(0, "", "Album was successfully saved", context, activity.goToDogDetails);
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
            public void onFailure(Call<DogProfileResponse> call, Throwable t) {
                Log.e("Error: ", t.getMessage());
            }
        });

    }

    public void getAlbum(DogProfile activity, Context context, ActivityDogProfileBinding binding, int dog_id){
        resp = new DogProfileResponse();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME, BuildConfig.API_PASS);
        Call<DogProfileResponse> call = services.getAlbum(dog_id);
        call.enqueue(new Callback<DogProfileResponse>() {
            @Override
            public void onResponse(Call<DogProfileResponse> call, Response<DogProfileResponse> response) {
                try {
                    albumList = new ArrayList<>();
                    if (response.code() == 200) {
                        resp = response.body();
                        if (resp.getStatusCode() == 200) {

                            binding.dogDetails.listView.setVisibility(View.VISIBLE);
                            binding.dogDetails.emptyContainer.setVisibility(View.GONE);

                            albumList = resp.getAlbums();
//                            toShowLayout(binding, 2);
//                            viewDogList(context, activity, binding);
                        } else {
                            binding.dogDetails.listView.setVisibility(View.GONE);
                            binding.dogDetails.emptyContainer.setVisibility(View.VISIBLE);
//                            AlertsAndLoaders alert = new AlertsAndLoaders();
//                            alert.showAlert(2, "", resp.getMessage(), context, null);
                        }
                    }

                    activity.getAlbumList(albumList);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DogProfileResponse> call, Throwable t) {
                Log.e("Error: ", t.getMessage());
            }
        });
    }

    public void saveAlbumImage(DogProfile activity, Context context, List<Uri> uri, int dog_album_id){
        resp = new DogProfileResponse();
        SharedPref util = new SharedPref();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME,BuildConfig.API_PASS);
        Call<DogProfileResponse> call = services.saveAlbumImage(getFilePart(uri, context), dog_album_id,  Integer.valueOf(util.readPrefString(context, util.USER_ID)));
        call.enqueue(new Callback<DogProfileResponse>() {
            @Override
            public void onResponse(Call<DogProfileResponse> call, Response<DogProfileResponse> response) {
                try {
                    if (response.code() == 200) {
                        resp = response.body();
                        if (resp.getStatusCode() == 200) {
                            AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                            alertsAndLoaders.showAlert(0, "", "Image was Successfully Saved", context, activity.goToAlbumList);
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
            public void onFailure(Call<DogProfileResponse> call, Throwable t) {
                Log.e("Error: ", t.getMessage());
            }
        });

    }

    public void getImageList(DogProfile activity, Context context, ActivityDogProfileBinding binding, int dog_album_id){
        resp = new DogProfileResponse();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME, BuildConfig.API_PASS);
        Call<DogProfileResponse> call = services.getAlbumImage(dog_album_id);
        call.enqueue(new Callback<DogProfileResponse>() {
            @Override
            public void onResponse(Call<DogProfileResponse> call, Response<DogProfileResponse> response) {
                try {
                    imageList = new ArrayList<>();
                    if (response.code() == 200) {
                        resp = response.body();
                        if (resp.getStatusCode() == 200) {

                            binding.albumContent.listView.setVisibility(View.VISIBLE);
                            binding.albumContent.emptyContainer.setVisibility(View.GONE);

                            imageList = resp.getImages();
//                            viewDogList(context, activity, binding);
                        } else {
                            binding.albumContent.listView.setVisibility(View.GONE);
                            binding.albumContent.emptyContainer.setVisibility(View.VISIBLE);
                            AlertsAndLoaders alert = new AlertsAndLoaders();
                            alert.showAlert(2, "", resp.getMessage(), context, null);
                        }
                    }

                    activity.getAlbumImageList(imageList);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DogProfileResponse> call, Throwable t) {
                Log.e("Error: ", t.getMessage());
            }
        });
    }




    public void toShowLayout(ActivityDogProfileBinding binding, int layout_id) {

        binding.dogList.getRoot().setVisibility(View.GONE);
        binding.dogDetails.getRoot().setVisibility(View.GONE);
        binding.albumContent.getRoot().setVisibility(View.GONE);
        binding.headerLayout.getRoot().setVisibility(View.VISIBLE);
        if (layout_id == 0) {

        } else if (layout_id == 1) {
            binding.dogList.getRoot().setVisibility(View.VISIBLE);
        } else if (layout_id == 2) {
            binding.dogDetails.getRoot().setVisibility(View.VISIBLE);
        }else if(layout_id == 3){
            binding.albumContent.getRoot().setVisibility(View.VISIBLE);
        }
    }

    private void viewDogList(Context context, DogProfile activity, ActivityDogProfileBinding binding) {
        try {
            DogListAdapter adapter = new DogListAdapter(context, activity, R.layout.dog_list_line, dogList);
            binding.dogList.listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<MultipartBody.Part> getFilePart(List<Uri> uri, Context context) {
        List<MultipartBody.Part> filePart = new ArrayList<>();
        for (Uri u : uri) {
            File file = compressFile(u, context);
            filePart.add(MultipartBody.Part.createFormData("file", new RotateImage().getFileNameFromUri(u, context), RequestBody.create(MediaType.parse("application/octet-stream"), file)));
        }

        return filePart;
    }
    private File compressFile(Uri imageUri, Context context) {
        File finalFile = null;
        try {
            Bitmap bmap = new RotateImage().toRotateBitmap(imageUri, context);
            Bitmap imageBitmap = bmap;

            int newWidth = 900;
            int newHeight = 1200;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, newWidth, newHeight, true);
            finalFile = new File(context.getCacheDir() + "/" + new RotateImage().getFileNameFromUri(imageUri, context));

            FileOutputStream fos = new FileOutputStream(finalFile);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalFile;
    }



}
