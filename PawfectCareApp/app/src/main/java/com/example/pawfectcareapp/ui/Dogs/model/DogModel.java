package com.example.pawfectcareapp.ui.Dogs.model;

import android.net.Uri;

import com.example.pawfectcareapp.BuildConfig;

import java.io.Serializable;

public class DogModel implements Serializable {

    private int id;
    private String dogName;
    String birthdate;
    String gender;
    String notes;
    Uri uri;

    String filepath;
    private String filename;


    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUri() {
        return BuildConfig.API_BASE_URL + "dogs/images/" + filename;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
