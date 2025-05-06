package com.example.pawfectcareapp.ui.Dogs.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pawfectcareapp.BuildConfig;
import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.ui.Dogs.model.DogModel;
import com.example.pawfectcareapp.ui.Dogs.view.DogProfile;
import com.example.pawfectcareapp.ui.Task.model.TaskModel;
import com.example.pawfectcareapp.ui.Task.view.TaskModule;

import java.io.File;
import java.util.List;

public class DogListAdapter extends ArrayAdapter<DogModel> {

    private Context mContext;
    int mResource;
    Activity mActivity;

    public DogListAdapter(Context context, Activity activity, int resource, List<DogModel> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            String dog_name = getItem(position).getDogName();
            String birthdate = getItem(position).getBirthdate();
            String gender = getItem(position).getGender();
            String filename = getItem(position).getFilename();


            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            TextView dog_name_txt = convertView.findViewById(R.id.dog_name);
            TextView birthdate_txt = convertView.findViewById(R.id.birthdate);
            TextView gender_txt = convertView.findViewById(R.id.gender);
            ImageView dog_image = convertView.findViewById(R.id.dog_image);


            dog_name_txt.setText(dog_name.toUpperCase());
            birthdate_txt.setText(formatDate(birthdate));
            gender_txt.setText(gender);
            show_image(dog_image, filename);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private String formatDate(String inputDate) {
        try {
            java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("MMMM d, yyyy");

            java.util.Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return inputDate; // fallback to original if parsing fails
        }
    }


    private void show_image(ImageView img, String filename) {
        try {
            String baseUrl = BuildConfig.API_BASE_URL; // e.g., http://192.168.1.8:8080/
            String imageUrl = baseUrl + "dogs/images/" + filename;

            Glide.with(mContext)
                    .load(imageUrl)   // optional error image
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
