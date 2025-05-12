package com.example.pawfectcareapp.ui.Task.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pawfectcareapp.Common.AlertsAndLoaders;
import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.Utils.FunctionInterface;
import com.example.pawfectcareapp.Utils.SharedPref;
import com.example.pawfectcareapp.databinding.ActivityTaskBinding;
import com.example.pawfectcareapp.ui.Task.Adapter.TaskListAdapter;
import com.example.pawfectcareapp.ui.Task.model.TaskModel;
import com.example.pawfectcareapp.ui.Task.viewModel.TaskViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskModule extends AppCompatActivity {

    ActivityTaskBinding binding;
    AlertDialog dialog = null;
    AutoCompleteTextView employee;
    TaskViewModel viewModel;
    List<TaskModel> taskList, searchTask, listOfSearchedTask;
    TaskModel selectedTask;
    int layout_id = 1;
    ArrayList<String> employee_arr;
    String task_status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        eventhandler();
    }

    private void init() {
        binding = ActivityTaskBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new TaskViewModel();
        taskList = new ArrayList<>();
        searchTask = new ArrayList<>();
        listOfSearchedTask = new ArrayList<>();

        employee_arr = new ArrayList<>();

        viewModel.getTask(binding, TaskModule.this, TaskModule.this);
        employee_arr = viewModel.getEmployees(TaskModule.this, TaskModule.this, binding);
        SharedPref util = new SharedPref();

        if(Integer.valueOf(util.readPrefString(TaskModule.this, SharedPref.ROLE_ID)) == 2){
            binding.taskList.addTask.setVisibility(View.GONE);
        }else{
            binding.taskList.addTask.setVisibility(View.VISIBLE);
        }


    }

    private void eventhandler() {

        binding.taskList.addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });

        binding.taskList.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listOfSearchedTask = new ArrayList<>();
                if (newText.equals("") || TextUtils.isEmpty(newText)) {
                    listOfSearchedTask = searchTask;
                } else {
                    for (TaskModel c : searchTask) {

                        if ((c.getTaskTitle() != null && c.getTaskTitle().toUpperCase().contains(newText.toUpperCase(Locale.ROOT))) || (c.getAssignedTo() != null && c.getAssignedTo().toUpperCase().contains(newText.toUpperCase(Locale.ROOT))) || (c.getAssignedFrom() != null && c.getAssignedFrom().toUpperCase().contains(newText.toUpperCase(Locale.ROOT)))) {
                            listOfSearchedTask.add(c);
                        }

                    }
                }

                viewTaskList(TaskModule.this, TaskModule.this, binding);
                return false;
            }
        });

        binding.taskList.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTask = listOfSearchedTask.get(position);
                SharedPref util = new SharedPref();
                if(Integer.valueOf(util.readPrefString(TaskModule.this, SharedPref.ROLE_ID)) == 1){
                    showFollowUpDialog();
                }else{
                    task_status = "COMPLETED";
                    AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                    alertsAndLoaders.showAlert(4, "Are you sure?", "You want to tag this task as COMPLETED?", TaskModule.this, updateTaskStatus);
                }
            }
        });

    }

    public void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskModule.this);
        View mview = getLayoutInflater().inflate(R.layout.add_task_dialog, null);
        EditText task_desc, task_title;
        CardView btn_cancel;
        LinearLayout assign_task;
        employee = mview.findViewById(R.id.employee);
        task_title = mview.findViewById(R.id.task_title);
        task_desc = mview.findViewById(R.id.task_desc);
        btn_cancel = mview.findViewById(R.id.btn_cancel);
        assign_task = mview.findViewById(R.id.assign_task);
//        -- SET EMPLOYEE IN DROP DOWN
        employee.setAdapter(new ArrayAdapter<>(TaskModule.this, android.R.layout.simple_list_item_1, employee_arr));

        assign_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!task_title.getText().toString().equals("") || !task_title.getText().toString().equals(null)
                        && !task_desc.getText().toString().equals("") || !task_desc.getText().toString().equals(null)
                        && !employee.getText().toString().equals("") || !employee.getText().toString().equals(null)) {

                    viewModel.assignTask(TaskModule.this, TaskModule.this, task_title.getText().toString(), task_desc.getText().toString(), employee.getText().toString());

                } else {
                    AlertsAndLoaders alert = new AlertsAndLoaders();
                    alert.showAlert(2, "", "All fields are required!", TaskModule.this, null);
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

    public void showFollowUpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskModule.this);
        View mview = getLayoutInflater().inflate(R.layout.remove_notif_task_dialog, null);
        TextView task_title;
        CardView btn_cancel, delete;
        LinearLayout follow_up;
        task_title = mview.findViewById(R.id.task_title);
        btn_cancel = mview.findViewById(R.id.btn_cancel);
        follow_up = mview.findViewById(R.id.follow_up);
        delete = mview.findViewById(R.id.delete);

        task_title.setText(selectedTask.getTaskTitle());

        follow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                alertsAndLoaders.showAlert(4,"Are you sure?", "You want to permanently delete this task?", TaskModule.this,followUpTask);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                alertsAndLoaders.showAlert(4,"Are you sure?", "You want to permanently delete this task?", TaskModule.this,deletetask);
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

    public void getTask(List<TaskModel> taskList) {
        this.taskList = taskList;
        searchTask = taskList;
        listOfSearchedTask = searchTask;
    }
//    public void getSearchTask(List<TaskModel> searchTask) {
//        this.searchTask = searchTask;
//    }

    public FunctionInterface.Function backToTaskList = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
//            dialog.cancel();
            layout_id = 1;
            viewModel.toShowLayout(binding, layout_id);
            viewModel.getTask(binding, TaskModule.this, TaskModule.this);
        }
    };

    public FunctionInterface.Function updateTaskStatus = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            viewModel.tagtaskAsCompleted(TaskModule.this, TaskModule.this, selectedTask.getId(), task_status);
        }
    };

    public FunctionInterface.Function deletetask = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            viewModel.deleteTask(TaskModule.this, TaskModule.this, selectedTask.getId());
        }
    };
    public FunctionInterface.Function followUpTask = new FunctionInterface.Function() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void perform() {
            viewModel.followUptask(TaskModule.this, TaskModule.this, selectedTask.getId());
        }
    };

    private void viewTaskList(Context context, TaskModule activity, ActivityTaskBinding binding) {
        try {
            binding.taskList.listView.setVisibility(View.VISIBLE);
            TaskListAdapter adapter = new TaskListAdapter(context, activity, R.layout.task_list_line, listOfSearchedTask);
            binding.taskList.listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}