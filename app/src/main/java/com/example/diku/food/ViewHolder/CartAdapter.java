package com.example.diku.food.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.diku.food.Cart;
import com.example.diku.food.Interface.ItemclickListener;
import com.example.diku.food.Module.Order;
import com.example.diku.food.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Diku on 20-05-2018.
 */

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView tvCartitemname,tvCartitemprice;
    public ImageView iv_cartitemimage;
    public ItemclickListener itemclickListener;

    public void setTvCartitemname(TextView tvCartitemname) {
        this.tvCartitemname = tvCartitemname;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        tvCartitemname=(TextView)itemView.findViewById(R.id.tvCartitemname);
        tvCartitemprice=(TextView)itemView.findViewById(R.id.tvCartitemprice);
        iv_cartitemimage=(ImageView)itemView.findViewById(R.id.iv_cartitemimage);

    }

    @Override
    public void onClick(View view) {

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
    public void onBindViewHolder(CartViewHolder holder, int position) {

        TextDrawable drawable=TextDrawable.builder()
                .buildRound(""+listdata.get(position).getQuantity(), Color.RED);
        holder.iv_cartitemimage.setImageDrawable(drawable);

        Locale locale=new Locale("en","US");
        NumberFormat fnt=NumberFormat.getCurrencyInstance(locale);
        int price=(Integer.parseInt(listdata.get(position).getPrice()))*(Integer.parseInt(listdata.get(position).getQuantity()));
        holder.tvCartitemprice.setText(fnt.format(price));

        holder.tvCartitemname.setText(listdata.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }
}
