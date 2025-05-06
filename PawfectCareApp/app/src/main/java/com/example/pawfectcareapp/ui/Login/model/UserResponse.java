package com.example.pawfectcareapp.ui.Login.model;

import java.util.List;

public class UserResponse {

    String message;
    String status;
    int statusCode;
    UserModel data;
    List<UserModel> employeeList;


    public List<UserModel> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<UserModel> employeeList) {
        this.employeeList = employeeList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public UserModel getData() {
        return data;
    }

    public void setData(UserModel data) {
        this.data = data;
    }
}
