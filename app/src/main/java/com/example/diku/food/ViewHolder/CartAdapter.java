package com.example.diku.food.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.diku.food.Cart;
import com.example.diku.food.Common.Common;
import com.example.diku.food.DataBase.Database;
import com.example.diku.food.Interface.ItemclickListener;
import com.example.diku.food.Module.Order;
import com.example.diku.food.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Diku on 20-05-2018.
 */

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{

    public TextView tvCartitemname,tvCartitemprice;
    public ImageView iv_cartitemimage,iv_cartitemdelete;
    public ItemclickListener itemclickListener;

    public void setTvCartitemname(TextView tvCartitemname) {
        this.tvCartitemname = tvCartitemname;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        tvCartitemname=(TextView)itemView.findViewById(R.id.tvCartitemname);
        tvCartitemprice=(TextView)itemView.findViewById(R.id.tvCartitemprice);
        iv_cartitemimage=(ImageView)itemView.findViewById(R.id.iv_cartitemimage);
        iv_cartitemdelete=(ImageView)itemView.findViewById(R.id.iv_cartitemdelete);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    public void setItemclickListener(ItemclickListener itemclickListener){

        this.itemclickListener=itemclickListener;
    }

    @Override
    public void onClick(View view) {

//        itemclickListener.onclick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.setHeaderTitle("Select the Action");
        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listdata=new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }


    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemview=inflater.inflate(R.layout.cart_layout,parent,false);

        return new CartViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final CartViewHolder holder, final int position) {


        TextDrawable drawable=TextDrawable.builder()
                .buildRound(""+listdata.get(position).getQuantity(), Color.RED);

        holder.iv_cartitemimage.setImageDrawable(drawable);


        Locale locale=new Locale("hi", "IN");
        NumberFormat fnt=NumberFormat.getCurrencyInstance(locale);
        double price=Double.parseDouble(listdata.get(position).getPrice());
        double discount=Double.parseDouble(listdata.get(position).getDiscount());
        double discounted_money=(price-(price*(discount/100)));
        double price1=(discounted_money)*(Integer.parseInt(listdata.get(position).getQuantity()));
        holder.tvCartitemprice.setText(fnt.format(price1));

        holder.tvCartitemname.setText(listdata.get(position).getProductName());

        holder.iv_cartitemdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order=listdata.get(position);
                new Database(context).deleteCart(order);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());



                //goes to new activity passing the item name
                Intent intent = new Intent(holder.itemView.getContext(), Cart.class);

                //begin activity
                holder.itemView.getContext().startActivity(intent);




            }
        });

    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }
}
