package com.example.diku.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.diku.food.Common.Common;
import com.example.diku.food.Interface.ItemclickListener;
import com.example.diku.food.Module.Request;
import com.example.diku.food.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
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

        firebaseDatabase =FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference("Requests");

        recyclerView=(RecyclerView)findViewById(R.id.listorders);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        loadOrders(Common.currentUser.getPhone());



    }

    private void loadOrders(String phone) {

        adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                reference.orderByChild("phone").equalTo(phone)) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {

                viewHolder.tvOrder_id.setText(adapter.getRef(position).getKey());
                viewHolder.tvOrder_address.setText(model.getAddress());
                viewHolder.tvOrder_phone.setText(model.getPhone());
                viewHolder.tvOrder_status.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.setItemclickListener(new ItemclickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongclick) {

                    }
                });

            }
        };

        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
    }
}