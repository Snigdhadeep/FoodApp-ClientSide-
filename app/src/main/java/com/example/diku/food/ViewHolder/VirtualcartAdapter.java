package com.example.diku.food.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diku.food.Module.Order;
import com.example.diku.food.R;

/**
 * Created by Diku on 06-06-2018.
 */

public class VirtualcartAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private Context context;
    Order order;

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemview=inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
