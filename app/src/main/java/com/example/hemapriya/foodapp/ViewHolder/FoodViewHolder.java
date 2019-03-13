package com.example.hemapriya.foodapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.hemapriya.foodapp.Interface.ItemClickListner;
import com.example.hemapriya.foodapp.R;

public class FoodViewHolder  extends RecyclerView.ViewHolder implements OnClickListener
{

    public TextView txtProductName,txtProductPrice;
    private ItemClickListner itemClickListner;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName=itemView.findViewById(R.id.product_name);
        txtProductPrice=itemView.findViewById(R.id.product_price);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
