package com.example.diku.food.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.diku.food.R;

/**
 * Created by Diku on 04-06-2018.
 */

public class CarouselAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

public ImageView img_couroselview;
    public CarouselAdapter(View itemView) {
        super(itemView);
        img_couroselview=(ImageView)itemView.findViewById(R.id.img_couroselview);
    }

    @Override
    public void onClick(View view) {

    }
}
