package com.example.pawfectcareapp.ui.Registration.model;

import com.example.pawfectcareapp.ui.Login.model.UserModel;

public class RegistrationResponse {

    String message;
    boolean status;
    int statusCode;
    RegistrationModel data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public RegistrationModel getData() {
        return data;
    }

    public void setData(RegistrationModel data) {
        this.data = data;
    }
}
