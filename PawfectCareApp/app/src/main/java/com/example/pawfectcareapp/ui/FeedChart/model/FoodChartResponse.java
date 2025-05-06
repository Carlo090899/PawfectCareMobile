package com.example.pawfectcareapp.ui.FeedChart.model;

import java.util.List;

public class FoodChartResponse {

    String message;
    boolean status;
    int statusCode;
    List<FoodChartModel> foodList;

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

    public List<FoodChartModel> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<FoodChartModel> foodList) {
        this.foodList = foodList;
    }
}
