package com.example.diku.food.ViewHolder;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diku.food.Interface.ItemclickListener;
import com.example.diku.food.R;

/**
 * Created by Diku on 16-05-2018.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

   public TextView txt_item_menu;
   public ImageView img_item_menu;

    //created a package "ItemclickListener"
     //created an interface into that package
      //onclick method created into that interface nd the mehod onclick holds three parameters ...
    private ItemclickListener itemclickListener;
    public MenuViewHolder(View itemView) {
        super(itemView);
        //created a layout .xml file "menuitem"
        txt_item_menu=(TextView)itemView.findViewById(R.id.txt2);
        img_item_menu=(ImageView) itemView.findViewById(R.id.image2);
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
