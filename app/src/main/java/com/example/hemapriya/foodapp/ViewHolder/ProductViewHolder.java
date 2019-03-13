package com.example.hemapriya.foodapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.hemapriya.foodapp.Interface.ItemClickListner;
import com.example.hemapriya.foodapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

{
    public TextView txtProductName, txtProductPrice;
    private ItemClickListner listner;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.product_name);
        txtProductPrice=itemView.findViewById(R.id.product_price);
    }
    public void setItemClickListner(ItemClickListner listner)
    {

        this.listner = listner;
    }
    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false);
    }
}