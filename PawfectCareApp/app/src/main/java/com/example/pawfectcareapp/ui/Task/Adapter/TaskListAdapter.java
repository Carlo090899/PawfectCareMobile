package com.example.pawfectcareapp.ui.Task.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.ui.Task.model.TaskModel;
import com.example.pawfectcareapp.ui.Task.view.TaskModule;

import java.util.List;

public class TaskListAdapter extends ArrayAdapter<TaskModel> {
    private Context mContext;
    int mResource;
    TaskModule mActivity;

    public TaskListAdapter(Context context, TaskModule activity, int resource, List<TaskModel> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            String title = getItem(position).getTaskTitle();
            String desc = getItem(position).getTaskDescription();
            String assignedTo = getItem(position).getAssignedTo();
            String assignedFrom = getItem(position).getAssignedFrom();
            String dateTime = getItem(position).getCreatedAt();
            String status = getItem(position).getStatus();


            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            TextView title_txt = convertView.findViewById(R.id.task);
            TextView desc_txt = convertView.findViewById(R.id.description);
            TextView assignedTo_txt = convertView.findViewById(R.id.assigned_to);
            TextView assignedFrom_txt = convertView.findViewById(R.id.assigned_from);
            TextView dateTime_txt = convertView.findViewById(R.id.dateTime);
            TextView status_txt = convertView.findViewById(R.id.status);

            if(status.contains("COMPLETED")){
                status_txt.setTextColor(Color.GREEN);
            }else{
                status_txt.setTextColor(Color.RED);
            }

            title_txt.setText(title);
            desc_txt.setText(desc);
            assignedTo_txt.setText(assignedTo);
            assignedFrom_txt.setText(assignedFrom);
            dateTime_txt.setText(dateTime);
            status_txt.setText(status);



        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

}
