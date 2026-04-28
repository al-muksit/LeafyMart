package com.leafymart.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leafymart.Adapter.PlantAdapter;
import com.leafymart.Database.DatabaseHelper;
import com.leafymart.Model.PlantModel;
import com.leafymart.R;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlantAdapter adapter;
    private List<PlantModel> favoritePlants;
    private DatabaseHelper dbHelper;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.favorites_recyclerview);
        dbHelper = new DatabaseHelper(requireContext());
        favoritePlants = new ArrayList<>();

        loadFavorites();

        adapter = new PlantAdapter(requireContext(), favoritePlants);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    private void loadFavorites() {
        favoritePlants.clear();
        Cursor cursor = dbHelper.getFavorites();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PLANT_NAME));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PLANT_PRICE));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PLANT_IMAGE));
                String rating = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PLANT_RATING));

                favoritePlants.add(new PlantModel(name, price, rating, "", "", image));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}