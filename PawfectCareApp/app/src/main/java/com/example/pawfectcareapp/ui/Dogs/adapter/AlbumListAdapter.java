package com.example.pawfectcareapp.ui.Dogs.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.pawfectcareapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawfectcareapp.databinding.ActivityDogProfileBinding;
import com.example.pawfectcareapp.ui.Dogs.model.DogAlbumModel;
import com.example.pawfectcareapp.ui.Dogs.view.DogProfile;

import java.util.List;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder>{
    List<DogAlbumModel> albumList;
    private Context mContext;
    private int mResource;
    ActivityDogProfileBinding bind;
    DogProfile mActivity;

    public AlbumListAdapter(Context context, int resource, List<DogAlbumModel> list, ActivityDogProfileBinding binding, DogProfile activity) {
        mContext = context;
        mResource = resource;
        albumList = list;
        bind = binding;
        mActivity = activity;
    }

    @NonNull
    @Override
    public AlbumListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        return new AlbumListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String album_name = albumList.get(position).getAlbumName();
        holder.album_name.setText(album_name);

        // Calculate 1/3 screen width
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int itemWidth = screenWidth / 3;

        // Set width dynamically
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = itemWidth;
        holder.itemView.setLayoutParams(layoutParams);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bind.albumContent.albumTitle.setText(album_name.toUpperCase());
                mActivity.goToAlbumContent(albumList.get(position).getId());
            }
        });

    }



    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView album_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            album_name = itemView.findViewById(R.id.album_name);
        }
    }
}
