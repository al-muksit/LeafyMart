package com.leafymart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.leafymart.Database.DatabaseHelper;
import com.leafymart.R;
import com.leafymart.Model.PlantModel;

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private Context context;
    private List<PlantModel> plants;
    private DatabaseHelper dbHelper;

    public PlantAdapter(Context context, List<PlantModel> plants) {
        this.context = context;
        this.plants = plants;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trending_items, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        PlantModel plant = plants.get(position);

        holder.trending_plants_IV.setImageResource(plant.getPlantImage());
        holder.trending_plants_name.setText(plant.getPlantName());
        holder.trending_plants_value.setText(plant.getPlantValue());
        holder.trending_plants_rating.setText(plant.getPlantRating());

        // Update Favorite Icon State
        if (dbHelper.isFavorite(plant.getPlantName())) {
            holder.btnFavorite.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
        }

        holder.btnFavorite.setOnClickListener(v -> {
            if (dbHelper.isFavorite(plant.getPlantName())) {
                dbHelper.removeFavorite(plant.getPlantName());
                holder.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
                Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addFavorite(plant.getPlantName(), plant.getPlantValue(), plant.getPlantImage(), plant.getPlantRating());
                holder.btnFavorite.setImageResource(R.drawable.baseline_favorite_24);
                Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnAddToCart.setOnClickListener(v -> {
            boolean success = dbHelper.addToCart(plant.getPlantName(), plant.getPlantValue(), plant.getPlantImage(), 1);
            if (success) {
                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to add to Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView trending_plants_IV, btnFavorite;
        TextView trending_plants_name;
        TextView trending_plants_value;
        TextView trending_plants_rating;
        MaterialButton btnAddToCart;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            trending_plants_IV = itemView.findViewById(R.id.trending_plants_IV);
            trending_plants_name = itemView.findViewById(R.id.trending_plants_name);
            trending_plants_value = itemView.findViewById(R.id.trending_plants_value);
            trending_plants_rating = itemView.findViewById(R.id.trending_plants_rating);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}