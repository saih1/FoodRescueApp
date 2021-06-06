package com.saihtoo.foodrescueapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saihtoo.foodrescueapp.R;
import com.saihtoo.foodrescueapp.model.CartItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

//RecyclerViewAdapter class for CartActivity.class
public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.ViewHolder> {
    List<CartItem> cartItemList;
    Context context;
    final int priceOfEachItem = 3;

    public CartViewAdapter(List<CartItem> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartViewAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(this.cartItemList.get(position).getImage());
        holder.titleText.setText(this.cartItemList.get(position).getTitle());
        holder.descriptionText.setText(this.cartItemList.get(position).getDescription());
        holder.dateText.setText(this.cartItemList.get(position).getDate());
        holder.quantityText.setText(this.cartItemList.get(position).getQuantity());
        holder.priceText.setText("$ " + priceOfEachItem);
    }

    @Override
    public int getItemCount() {
        return this.cartItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleText, descriptionText, dateText, quantityText, priceText;
        public ViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.cartImageView);
            titleText = view.findViewById(R.id.cartNameText);
            descriptionText = view.findViewById(R.id.cartDescriptionText);
            dateText = view.findViewById(R.id.cartDateText);
            quantityText = view.findViewById(R.id.cartQuantityText);
            priceText = view.findViewById(R.id.cartPriceText);
        }
    }
}
