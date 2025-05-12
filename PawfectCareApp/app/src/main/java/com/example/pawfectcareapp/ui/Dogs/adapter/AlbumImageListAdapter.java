package com.example.pawfectcareapp.ui.Dogs.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pawfectcareapp.BuildConfig;
import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.databinding.ActivityDogProfileBinding;
import com.example.pawfectcareapp.ui.Dogs.model.AlbumImagesModel;
import com.example.pawfectcareapp.ui.Dogs.model.DogAlbumModel;
import com.example.pawfectcareapp.ui.Dogs.view.DogProfile;

import java.util.List;

public class AlbumImageListAdapter extends RecyclerView.Adapter<AlbumImageListAdapter.ViewHolder>{
    List<AlbumImagesModel> imageList;
    private Context mContext;
    private int mResource;
    ActivityDogProfileBinding bind;
    DogProfile mActivity;

    public AlbumImageListAdapter(Context context, int resource, List<AlbumImagesModel> list, ActivityDogProfileBinding binding, DogProfile activity) {
        mContext = context;
        mResource = resource;
        imageList = list;
        bind = binding;
        mActivity = activity;
    }

    @NonNull
    @Override
    public AlbumImageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        return new AlbumImageListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumImageListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String filename = imageList.get(position).getFilename();
        String baseUrl = BuildConfig.API_BASE_URL;
        String imageUrl = baseUrl + "dogs/images/" + filename;

        show_image(holder.image, filename);

        holder.image.setOnClickListener(v -> {
            mActivity.showImageInFullScreen(imageUrl);
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);


        }
    }

    private void show_image(ImageView img, String filename) {
        try {
            String baseUrl = BuildConfig.API_BASE_URL; // e.g., http://192.168.1.8:8080/
            String imageUrl = baseUrl + "dogs/images/" + filename;

            Glide.with(mContext)
                    .load(imageUrl)   // optional error image
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
