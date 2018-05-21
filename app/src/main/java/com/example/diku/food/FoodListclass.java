package com.example.diku.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.diku.food.Interface.ItemclickListener;
import com.example.diku.food.Module.FoodList;
import com.example.diku.food.ViewHolder.FoodViewHolder;
import com.example.diku.food.ViewHolder.FoodViewHolder2;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodListclass extends AppCompatActivity {

    String categoryid="";

    FirebaseDatabase firebaseDatabase;
  public   DatabaseReference fooditems;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

   FirebaseRecyclerAdapter<FoodList,FoodViewHolder2> adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        firebaseDatabase=FirebaseDatabase.getInstance();
        fooditems=firebaseDatabase.getReference("Foods");

        recyclerView=(RecyclerView)findViewById(R.id.rv_foodlist);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent()!=null) {

            categoryid = getIntent().getStringExtra("foodkey");
        }

        if(!categoryid.isEmpty() && categoryid!= null){

            loadlist(categoryid);
            Toast.makeText(this, "function worked"+categoryid, Toast.LENGTH_SHORT).show();
        }


    }



    private void loadlist(String categoryid) {


        adapter=new FirebaseRecyclerAdapter<FoodList, FoodViewHolder2>(
                FoodList.class,
                R.layout.fooditem,
                FoodViewHolder2.class,
                fooditems.orderByChild("menuid").equalTo(categoryid)

        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder2 viewHolder, FoodList model, int position) {
                viewHolder.txt_item_food.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.img_item_food);


                final FoodList local=model;
                viewHolder.setItemclickListener(new ItemclickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongclick) {
                        Intent intent=new Intent(FoodListclass.this,FoodDetails.class);
                        intent.putExtra("foodlistId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });


            }
        };




recyclerView.setAdapter(adapter);


    }

}
