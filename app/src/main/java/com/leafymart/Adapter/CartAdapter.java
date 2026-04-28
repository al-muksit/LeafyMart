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

import com.leafymart.Database.DatabaseHelper;
import com.leafymart.Model.CartItemModel;
import com.leafymart.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItemModel> cartItems;
    private DatabaseHelper dbHelper;
    private OnCartChangedListener listener;

    public interface OnCartChangedListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, List<CartItemModel> cartItems, OnCartChangedListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.dbHelper = new DatabaseHelper(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemModel item = cartItems.get(position);

        holder.image.setImageResource(item.getImage());
        holder.name.setText(item.getName());
        holder.price.setText(item.getPrice());
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.removeFromCart(item.getId());
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            if (listener != null) listener.onCartChanged();
            Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();
        });
        
        // Add/Minus logic can be added here as well
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView image, btnDelete, btnAdd;
        TextView name, price, quantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cart_item_image);
            name = itemView.findViewById(R.id.cart_item_name);
            price = itemView.findViewById(R.id.cart_item_price);
            quantity = itemView.findViewById(R.id.tv_cart_quantity);
            btnDelete = itemView.findViewById(R.id.btn_cart_delete);
            btnAdd = itemView.findViewById(R.id.btn_cart_add);
        }
    }
}