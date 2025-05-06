package com.example.pawfectcareapp.ui.Team.model;

import java.util.List;

public class TeamResponse {

    String message;
    String status;
    int statusCode;

    List<TeamModel> teamList;

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

    public List<TeamModel> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<TeamModel> teamList) {
        this.teamList = teamList;
    }
}
