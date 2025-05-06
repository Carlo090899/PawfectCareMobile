package com.example.pawfectcareapp.ui.Dogs.model;


import java.util.List;

public class DogProfileResponse {

    String message;
    boolean status;
    int statusCode;

    private List<DogModel> dogs;
    private List<DogAlbumModel> albums;
    private List<AlbumImagesModel> images;


    public List<AlbumImagesModel> getImages() {
        return images;
    }

    public void setImages(List<AlbumImagesModel> images) {
        this.images = images;
    }

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


    public List<DogModel> getDogs() {
        return dogs;
    }

    public void setDogs(List<DogModel> dogs) {
        this.dogs = dogs;
    }

    public List<DogAlbumModel> getAlbums() {
        return albums;
    }

    public void setAlbums(List<DogAlbumModel> albums) {
        this.albums = albums;
    }
}
