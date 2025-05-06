package com.example.pawfectcareapp.ui.FeedChart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.databinding.ActivityDogProfileBinding;
import com.example.pawfectcareapp.databinding.ActivityFeedChartBinding;
import com.example.pawfectcareapp.ui.Dogs.adapter.AlbumListAdapter;
import com.example.pawfectcareapp.ui.Dogs.model.DogAlbumModel;
import com.example.pawfectcareapp.ui.Dogs.view.DogProfile;
import com.example.pawfectcareapp.ui.FeedChart.model.FoodChartModel;
import com.example.pawfectcareapp.ui.FeedChart.view.FoodChart;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder>{
    List<FoodChartModel> foodList;
    private Context mContext;
    private int mResource;
    ActivityFeedChartBinding bind;
    FoodChart mActivity;
    int role;

    public FoodListAdapter(Context context, int resource, List<FoodChartModel> list, ActivityFeedChartBinding binding, FoodChart activity, int role_id) {
        mContext = context;
        mResource = resource;
        foodList = list;
        bind = binding;
        mActivity = activity;
        role = role_id;
    }

    @NonNull
    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        return new FoodListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String dog_food = foodList.get(position).getDogFood();
        String food_desc = foodList.get(position).getFoodDesc();
        String quantity = foodList.get(position).getQuantity();
        holder.dog_food.setText(dog_food);
        holder.food_desc.setText(food_desc);
        holder.quantity.setText(quantity);

        if(role != 1){
            holder.btn_delete.setVisibility(View.GONE);
        }

        // Calculate 1/3 screen width
//        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
//        int itemWidth = screenWidth / 3;

        // Set width dynamically
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
//        layoutParams.width = itemWidth;
        holder.itemView.setLayoutParams(layoutParams);

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.deleteFood(foodList.get(position).getId());
            }
        });

    }



    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dog_food, food_desc, quantity;
        ImageView btn_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dog_food = itemView.findViewById(R.id.dog_food);
            food_desc = itemView.findViewById(R.id.food_desc);
            quantity = itemView.findViewById(R.id.quantity);
            btn_delete = itemView.findViewById(R.id.delete);
        }
    }
}
