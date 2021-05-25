package com.saihtoo.foodrescueapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saihtoo.foodrescueapp.R;
import com.saihtoo.foodrescueapp.model.FoodItem;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.AdapterViewHolder> {
    List<FoodItem> foodItemList;
    Context context;

    public RecyclerViewAdapter(List<FoodItem> foodItemList, Context context) {
        this.foodItemList = foodItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.AdapterViewHolder holder, int position) {
        holder.itemView.setImageBitmap(this.foodItemList.get(position).getImage());
        holder.itemTitle.setText(this.foodItemList.get(position).getTitle());
        holder.itemDescription.setText(this.foodItemList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return this.foodItemList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemView;
        public TextView itemTitle, itemDescription;
        public ImageButton shareButton;

        public AdapterViewHolder(@NonNull View view) {
            super(view);
            itemView = view.findViewById(R.id.imageView);
            itemTitle = view.findViewById(R.id.itemNameText);
            itemDescription = view.findViewById(R.id.itemDescriptionText);
            shareButton = view.findViewById(R.id.itemShareButton);
        }
    }
}
