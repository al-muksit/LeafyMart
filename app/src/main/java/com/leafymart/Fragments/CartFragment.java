package com.leafymart.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leafymart.Adapter.CartAdapter;
import com.leafymart.Database.DatabaseHelper;
import com.leafymart.Model.CartItemModel;
import com.leafymart.R;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnCartChangedListener {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItemModel> cartItems;
    private DatabaseHelper dbHelper;
    private TextView tvTotalPrice;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.cart_recyclerview);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        dbHelper = new DatabaseHelper(requireContext());
        cartItems = new ArrayList<>();

        loadCartItems();

        adapter = new CartAdapter(requireContext(), cartItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.btn_checkout).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Proceeding to Checkout...", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadCartItems() {
        cartItems.clear();
        Cursor cursor = dbHelper.getCartItems();
        int total = 0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PLANT_NAME));
                String priceStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PLANT_PRICE));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PLANT_IMAGE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY));

                cartItems.add(new CartItemModel(id, name, priceStr, image, quantity));

                // Basic price calculation (strip '৳' and parse)
                try {
                    int price = Integer.parseInt(priceStr.replace("৳", "").trim());
                    total += price * quantity;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
            cursor.close();
        }
        tvTotalPrice.setText("৳" + total);
    }

    @Override
    public void onCartChanged() {
        calculateTotal();
    }

    private void calculateTotal() {
        int total = 0;
        for (CartItemModel item : cartItems) {
            try {
                int price = Integer.parseInt(item.getPrice().replace("৳", "").trim());
                total += price * item.getQuantity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tvTotalPrice.setText("৳" + total);
    }
}