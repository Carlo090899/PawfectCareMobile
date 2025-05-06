package com.example.pawfectcareapp.ui.Dogs.model;

import java.io.Serializable;

public class AlbumImagesModel implements Serializable {

    int id;
    String filename;
    String filepath;
    int dogAlbumId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getDogAlbumId() {
        return dogAlbumId;
    }

    public void setDogAlbumId(int dogAlbumId) {
        this.dogAlbumId = dogAlbumId;
    }
}
