package com.example.pawfectcareapp.ui.Team.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.ui.Dogs.model.DogModel;
import com.example.pawfectcareapp.ui.Task.view.TaskModule;
import com.example.pawfectcareapp.ui.Team.model.TeamModel;

import java.util.List;

public class TeamListAdapter extends ArrayAdapter<TeamModel> {

    private Context mContext;
    int mResource;
    Activity mActivity;
    int mRoleId;

    public TeamListAdapter(Context context, Activity activity, int resource, List<TeamModel> objects, int role_id) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mActivity = activity;
        mRoleId = role_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            String fullname = getItem(position).getFullname();
            String gender = getItem(position).getGender();
            boolean isActive = getItem(position).isActive();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            TextView fullname_txt = convertView.findViewById(R.id.name);
            ImageView avatar = convertView.findViewById(R.id.avatar);
            TextView status =  convertView.findViewById(R.id.status);

            fullname_txt.setText(fullname.toUpperCase());

            if (isActive) {
                status.setText("Active");
                status.setTextColor(Color.GREEN);
            } else {
                status.setText("Inactive");
                status.setTextColor(Color.RED);
            }

            if(gender.contains("Male")){
                avatar.setImageResource(R.drawable.man);
            }else{
                avatar.setImageResource(R.drawable.woman);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
