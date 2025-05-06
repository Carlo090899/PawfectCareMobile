package com.example.pawfectcareapp.ui.Dogs.model;

import java.io.Serializable;

public class DogAlbumModel implements Serializable {

    int id;
    String albumName;
    int dogId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getDogId() {
        return dogId;
    }

    public void setDogId(int dogId) {
        this.dogId = dogId;
    }
}
