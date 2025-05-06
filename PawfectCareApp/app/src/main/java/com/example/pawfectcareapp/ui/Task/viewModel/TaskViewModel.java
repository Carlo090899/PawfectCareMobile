package com.example.pawfectcareapp.ui.Task.viewModel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.pawfectcareapp.API.ApiCall;
import com.example.pawfectcareapp.API.ServiceGenerator;
import com.example.pawfectcareapp.BuildConfig;
import com.example.pawfectcareapp.Common.AlertsAndLoaders;
import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.Utils.SharedPref;
import com.example.pawfectcareapp.databinding.ActivityTaskBinding;
import com.example.pawfectcareapp.ui.Login.model.UserModel;
import com.example.pawfectcareapp.ui.Login.model.UserResponse;
import com.example.pawfectcareapp.ui.Task.Adapter.TaskListAdapter;
import com.example.pawfectcareapp.ui.Task.model.TaskModel;
import com.example.pawfectcareapp.ui.Task.model.TaskResponse;
import com.example.pawfectcareapp.ui.Task.view.TaskModule;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskViewModel {

    TaskResponse resp;
    UserResponse uResp;
    List<TaskModel> taskList;
    List<UserModel> employees;

    public void getTask(ActivityTaskBinding binding, TaskModule activity, Context context) {
        resp = new TaskResponse();
        SharedPref util = new SharedPref();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME, BuildConfig.API_PASS);
        Call<TaskResponse> call = services.getTask(util.readPrefString(context, util.FULLNAME), Integer.valueOf(util.readPrefString(context, util.ROLE_ID)));
        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                try {
                    taskList = new ArrayList<>();
                    if (response.code() == 200) {
                        resp = response.body();
                        if (resp.getStatusCode() == 200) {

                            binding.taskList.listView.setVisibility(View.VISIBLE);
                            binding.taskList.emptyContainer.setVisibility(View.GONE);

                            taskList = resp.getTasks();
                            toShowLayout(binding, 1);
                            viewTaskList(context, activity, binding);
                        } else {
                            AlertsAndLoaders alert = new AlertsAndLoaders();
                            alert.showAlert(2, "", resp.getMessage(), context, null);
                        }
                    }

                    activity.getTask(taskList);
//                    activity.getSearchTask(taskList);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Log.e("Error: ", t.getMessage());
            }
        });
    }

    public void assignTask(TaskModule activity, Context context, String task_title, String task_desc, String employee) {
        resp = new TaskResponse();
        SharedPref util = new SharedPref();
        ApiCall services = ServiceGenerator.createService(ApiCall.class, BuildConfig.API_UNAME, BuildConfig.API_PASS);
        Call<TaskResponse> call = services.assigntask(task_title, task_desc, employee, util.readPrefString(context, util.FULLNAME),Integer.valueOf(util.readPrefString(context, util.USER_ID)));
        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                try {
                    if (response.code() == 200) {
                        resp = response.body();
                        if (resp.getStatusCode() == 200) {
                            AlertsAndLoaders alert = new AlertsAndLoaders();
                            alert.showAlert(0, "", resp.getMessage(), context, activity.backToTaskList);
                        } else {
                            AlertsAndLoaders alert = new AlertsAndLoaders();
                            alert.showAlert(2, "", resp.getMessage(), context, null);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Log.e("Error: ", t.getMessage());
            }
        });
    }

    public void toShowLayout(ActivityTaskBinding binding, int layout_id) {

        binding.taskList.getRoot().setVisibility(View.GONE);
        binding.headerLayout.getRoot().setVisibility(View.VISIBLE);
        if (layout_id == 0) {

        } else if (layout_id == 1) {
            binding.taskList.getRoot().setVisibility(View.VISIBLE);
        } else if (layout_id == 2) {
        }
    }

    private void viewTaskList(Context context, TaskModule activity, ActivityTaskBinding binding) {
        try {
            binding.taskList.listView.setVisibility(View.VISIBLE);
            TaskListAdapter adapter = new TaskListAdapter(context, activity, R.layout.task_list_line, taskList);
            binding.taskList.listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getEmployees(Context context, TaskModule activity, ActivityTaskBinding binding) {
        uResp = new UserResponse();
        ArrayList<String> employee_arr = new ArrayList<String>();

        AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();

        ApiCall services = ServiceGenerator.createService(ApiCall.class, "", "");
        Call<UserResponse> call = services.getEmployeeList();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                try {
                    employees = new ArrayList<>();
                    if (response.code() == 200) {
                        uResp = response.body();
                        if (uResp.getStatusCode() == 200) {
                            employees = uResp.getEmployeeList();
                            for (int i = 0; i < employees.size(); i++) {
                                employee_arr.add(employees.get(i).getFullname());
                            }
                        } else {
                            alertsAndLoaders.showAlert(2, "","No Employees Found", context, null);
                        }
                    } else {
//                        DISPLAY ERROR HERE.....
                        alertsAndLoaders.showAlert(2, "", "No Employees Found", context, null);
                    }
//                    activity.getMawbs(mawbs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Error: ", t.getMessage());
            }
        });

        return employee_arr;
    }

    public void tagtaskAsCompleted(Context context, TaskModule activity, int task_id, String status) {
        AlertsAndLoaders alert = new AlertsAndLoaders();
        SweetAlertDialog sDialog = alert.showAlert(3, "Loading . . .", "Saving please wait", context, null);
        ApiCall service = ServiceGenerator.createService(ApiCall.class, "", "");
        Call<TaskResponse> call = service.tagTaskAsCompleted(task_id, status);
        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {

                try {
                    TaskResponse res = new TaskResponse();
                    res = response.body();
                    if (res.getStatusCode() == 200) {
                        sDialog.cancel();
                        AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                        alertsAndLoaders.showAlert(0, "", res.getMessage(), context, activity.backToTaskList);

                    } else {
                        sDialog.cancel();
                        AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                        alertsAndLoaders.showAlert(1, "", res.getMessage(), context, null);
                    }

                } catch (Exception e) {
                    sDialog.cancel();
                    e.printStackTrace();
                    AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                    alertsAndLoaders.showAlert(2, "", e.getMessage(), context, null);
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                sDialog.cancel();
                AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                alertsAndLoaders.showAlert(2, "", t.getMessage(), context, null);
            }
        });

    }

    public void deleteTask(Context context, TaskModule activity, int task_id) {
        AlertsAndLoaders alert = new AlertsAndLoaders();
        SweetAlertDialog sDialog = alert.showAlert(3, "Loading . . .", "Saving please wait", context, null);
        ApiCall service = ServiceGenerator.createService(ApiCall.class, "", "");
        Call<TaskResponse> call = service.deleteTask(task_id);
        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {

                try {
                    TaskResponse res = new TaskResponse();
                    res = response.body();
                    if (res.getStatusCode() == 200) {
                        sDialog.cancel();
                        AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                        alertsAndLoaders.showAlert(0, "", res.getMessage(), context, activity.backToTaskList);
                    } else {
                        sDialog.cancel();
                        AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                        alertsAndLoaders.showAlert(1, "", res.getMessage(), context, activity.backToTaskList);
                    }

                } catch (Exception e) {
                    sDialog.cancel();
                    e.printStackTrace();
                    AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                    alertsAndLoaders.showAlert(2, "", e.getMessage(), context, null);
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                sDialog.cancel();
                AlertsAndLoaders alertsAndLoaders = new AlertsAndLoaders();
                alertsAndLoaders.showAlert(2, "", t.getMessage(), context, null);
            }
        });

    }

}
