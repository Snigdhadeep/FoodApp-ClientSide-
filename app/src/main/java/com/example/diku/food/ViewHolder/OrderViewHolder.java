package com.example.diku.food.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diku.food.Interface.ItemclickListener;
import com.example.diku.food.R;

/**
 * Created by Diku on 26-05-2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

   public TextView tvOrder_id,tvOrder_status,tvOrder_phone,tvOrder_address;
    private ItemclickListener itemclickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        tvOrder_id=(TextView)itemView.findViewById(R.id.tvOrder_id);
        tvOrder_status=(TextView)itemView.findViewById(R.id.tvOrder_status);
        tvOrder_phone=(TextView)itemView.findViewById(R.id.tvOrder_phone);
        tvOrder_address=(TextView)itemView.findViewById(R.id.tvOrder_address);

        itemView.setOnClickListener(this);



    }

    public void setItemclickListener(ItemclickListener itemclickListener) {
        this.itemclickListener = itemclickListener;
    }

    @Override
    public void onClick(View view) {

        itemclickListener.onclick(view,getAdapterPosition(),false);


    }
}
