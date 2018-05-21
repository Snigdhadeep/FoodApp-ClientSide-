package com.example.diku.food.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diku.food.Interface.ItemclickListener;
import com.example.diku.food.R;

/**
 * Created by Diku on 16-05-2018.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

   public TextView txt_item_food;
   public ImageView img_item_food;

    //created a package "ItemclickListener"
     //created an interface into that package
      //onclick method created into that interface nd the mehod onclick holds three parameters ...
    private ItemclickListener itemclickListener;
    public FoodViewHolder(View itemView) {
        super(itemView);
        //created a layout .xml file "menuitem"
        txt_item_food=(TextView)itemView.findViewById(R.id.txt_fooditem);
        img_item_food=(ImageView) itemView.findViewById(R.id.image_fooditem);
        itemView.setOnClickListener(this);


    }
//ugenerating setter method
    public void setItemclickListener(ItemclickListener itemclickListener) {
        this.itemclickListener = itemclickListener;
    }

    @Override
    public void onClick(View view) {


        itemclickListener.onclick(view,getAdapterPosition(),false); //...here the three parameters have been used

    }
}
