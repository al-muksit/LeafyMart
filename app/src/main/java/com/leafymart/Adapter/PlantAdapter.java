package com.leafymart.Adapter;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.leafymart.R;
import com.leafymart.Model.PlantModel;

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private Context context;
    private List<PlantModel> plants;

    public PlantAdapter(Context context, List<PlantModel> plants) {
        this.context = context;
        this.plants = plants;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trending_items, parent, false);
        return new PlantViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        PlantModel plant = plants.get(position);

        holder.trending_plants_name.setText(plant.getName());
        holder.trending_plants_value.setText("৳" + plant.getPrice());
        holder.trending_plants_rating.setText(String.valueOf(plant.getRating()));
        holder.trending_plants_people_rates.setText("(N/A)");
        holder.trending_plants_sold.setText(String.valueOf(plant.getSold()) + " Sold");


        Glide.with(context)
                .load(plant.getImageUrl())
                .placeholder(R.drawable.baseline_star_16)
                .error(R.drawable.baseline_notifications_24)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GlideError", "Image load failed: " + plant.getImageUrl(), e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        Log.d("GlideSuccess", "Image loaded: " + plant.getImageUrl());
                        return false;
                    }
                })
                .into(holder.trending_plants_IV);



    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView trending_plants_IV;
        TextView trending_plants_name;
        TextView trending_plants_value;
        TextView trending_plants_rating;
        TextView trending_plants_people_rates;
        TextView trending_plants_sold;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            trending_plants_IV = itemView.findViewById(R.id.trending_plants_IV);
            trending_plants_name = itemView.findViewById(R.id.trending_plants_name);
            trending_plants_value = itemView.findViewById(R.id.trending_plants_value);
            trending_plants_rating = itemView.findViewById(R.id.trending_plants_rating);
            trending_plants_people_rates = itemView.findViewById(R.id.trending_plants_people_rates);
            trending_plants_sold = itemView.findViewById(R.id.trending_plants_sold);
        }
    }
}