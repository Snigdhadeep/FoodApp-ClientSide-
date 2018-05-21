package com.example.diku.food;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diku.food.Common.Common;
import com.example.diku.food.DataBase.Database;
import com.example.diku.food.Module.Order;
import com.example.diku.food.Module.Request;
import com.example.diku.food.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request_ref;


    TextView tv_total;
    Button bt_placeorder;
    List<Order> cart=new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database=FirebaseDatabase.getInstance();
        request_ref=database.getReference("Requests");

        recyclerView=(RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        tv_total=(TextView)findViewById(R.id.tv_total);
        bt_placeorder=(Button)findViewById(R.id.bt_placeorder);

        loadListFood();


        bt_placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showAlertDialog();
            }
        });


    }

    private void showAlertDialog() {

        AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
        alertdialog.setTitle("One last step");
        alertdialog.setMessage("your Address");
        alertdialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        final EditText etAddress=new EditText(this);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT

        );

        etAddress.setLayoutParams(layoutParams);
        alertdialog.setView(etAddress);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Request request_class=new Request(

                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        etAddress.getText().toString(),
                        tv_total.getText().toString(),
                        cart


                );


                //submit to firebase
                //using current time as key

                request_ref.child(String.valueOf(System.currentTimeMillis())).setValue(request_class);
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Tank you, Order placed ", Toast.LENGTH_SHORT).show();
            }
        });


        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertdialog.show();

    }

    private void loadListFood() {


        cart=new Database(this).getCarts();
        adapter=new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        //calculate total price
        int total=0;
        for(Order order:cart){

            Log.i("price",order.getPrice());
            Log.i("price",order.getQuantity());

            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
            Locale locale=new Locale("en","US");
            NumberFormat fnt=NumberFormat.getCurrencyInstance(locale);

            tv_total.setText(fnt.format(total));
        }
    }


}