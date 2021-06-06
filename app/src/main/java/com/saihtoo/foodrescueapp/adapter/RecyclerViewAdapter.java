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

//RecyclerViewAdapter for HomeActivity.class and MyListActivity.class
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.AdapterViewHolder> {
    List<FoodItem> foodItemList;
    Context context;
    onFoodItemClickListener onFoodItemClickListener;

    public RecyclerViewAdapter(List<FoodItem> foodItemList, Context context, onFoodItemClickListener listener) {
        this.foodItemList = foodItemList;
        this.context = context;
        this.onFoodItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item, parent, false);
        return new AdapterViewHolder(view, onFoodItemClickListener);
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

    public static class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView itemView;
        public TextView itemTitle, itemDescription;
        public ImageButton shareButton;
        onFoodItemClickListener onFoodItemClickListener;

        public AdapterViewHolder(@NonNull View view, onFoodItemClickListener listener) {
            super(view);
            itemView = view.findViewById(R.id.imageView);
            itemTitle = view.findViewById(R.id.itemNameText);
            itemDescription = view.findViewById(R.id.itemDescriptionText);
            shareButton = view.findViewById(R.id.itemShareButton);
            onFoodItemClickListener = listener;
            view.setOnClickListener(this);
            shareButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //check if your clicks on the View or the Share button
            if (v.getId() == R.id.itemShareButton)
                onFoodItemClickListener.onShareClick(getAdapterPosition());
            else
                onFoodItemClickListener.onRowClick(getAdapterPosition());
        }
    }

    public interface onFoodItemClickListener {
        void onRowClick(int position);
        void onShareClick(int position);
    }
}
