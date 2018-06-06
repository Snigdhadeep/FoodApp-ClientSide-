package com.example.diku.food;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.diku.food.Common.Common;
import com.example.diku.food.DataBase.Database;
import com.example.diku.food.DataBase.Databasesingle;
import com.example.diku.food.Module.FoodList;
import com.example.diku.food.Module.Order;
import com.example.diku.food.Module.Request;
import com.example.diku.food.Module.SingleRequest;
import com.example.diku.food.ViewHolder.CartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodDetails extends AppCompatActivity {


    TextView foodname_details,txtfoodprice_details,txtfooddescription_details,total2,pay;
    ImageView img_fooddetails;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ElegantNumberButton btn_elegant;
    



    String foodlistId="";
    FoodList currentfoodList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference fooddetails_ref;
    
    DatabaseReference  request_ref;

    int totalmoney=0;


    List<Order> virtualcart=new ArrayList<>();
    CartAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseDatabase=FirebaseDatabase.getInstance();
        fooddetails_ref=firebaseDatabase.getReference("Foods");
        request_ref=firebaseDatabase.getReference("Requests");

      //  elegantNumberButton=(ElegantNumberButton)findViewById(R.id.btn_elegant);

        foodname_details=(TextView)findViewById(R.id.foodname_details);
        txtfoodprice_details=(TextView)findViewById(R.id.txtfoodprice_details);
        txtfooddescription_details=(TextView)findViewById(R.id.txtfooddescription_details);
        total2=(TextView)findViewById(R.id.total2);
        pay=(TextView)findViewById(R.id.pay);


        btn_elegant=(ElegantNumberButton)findViewById(R.id.btn_elegant);

        btn_elegant.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
               loadtotalmoney(newValue);
            }
        });
        
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showAlertDialog();
            }
        });


        img_fooddetails=(ImageView)findViewById(R.id.img_fooddetails);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);

        FloatingActionButton fabAddtocart = (FloatingActionButton) findViewById(R.id.fab_cart_details);
        fabAddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_elegant.getNumber() != "0") {

                    new Database(getBaseContext()).addToCart(new Order(

                            foodlistId,
                            currentfoodList.getName(),

                            btn_elegant.getNumber(),
                            currentfoodList.getPrice(),
                            currentfoodList.getDiscount()

                    ));


                    Toast.makeText(FoodDetails.this, "added to cart " + foodlistId, Toast.LENGTH_SHORT).show();
                    Log.i("price3", currentfoodList.getPrice());
                   // new Database(getBaseContext()).cleanCart();

                    Intent intent=new Intent(getApplicationContext(),Cart.class);
                    startActivity(intent);

                }else {

                    Toast.makeText(FoodDetails.this, "please,add number of items", Toast.LENGTH_SHORT).show();
                }

            }

        });






        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);

        if(getIntent()!=null){

            foodlistId=getIntent().getStringExtra("foodlistId");

        }

        if(!foodlistId.isEmpty()){
            
            getdetailfood(foodlistId);

        }

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

                new Databasesingle(getBaseContext()).addToCart(new Order(

                        foodlistId,
                        currentfoodList.getName(),

                        btn_elegant.getNumber(),
                        currentfoodList.getPrice(),
                        currentfoodList.getDiscount()

                ));


                virtualcart=new Databasesingle(FoodDetails.this).getCarts();
                adapter=new CartAdapter(virtualcart,FoodDetails.this);
                Request request_class=new Request(

                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        etAddress.getText().toString(),
                        total2.getText().toString(),
                        virtualcart


                );


                //submit to firebase
                //using current time as key

                request_ref.child(String.valueOf(System.currentTimeMillis())).setValue(request_class);
                new Databasesingle(getBaseContext()).cleanCart();
                Toast.makeText(FoodDetails.this, "Tank you, Order placed ", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),OrderStatus.class);
                startActivity(intent);
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
    private void loadtotalmoney(int newValue) {


        totalmoney=(newValue)*(Integer.parseInt(currentfoodList.getPrice()));
        Locale locale=new Locale("en","US");
        NumberFormat fnt=NumberFormat.getCurrencyInstance(locale);

        total2.setText(totalmoney+"টাকা");




    }

    private void getdetailfood(final String foodlistId) {

        fooddetails_ref.child(foodlistId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 currentfoodList=dataSnapshot.getValue(FoodList.class);

                Picasso.with(getBaseContext()).load(currentfoodList.getImage()).into(img_fooddetails);
                //foodname_details,txtfoodprice_details,txtfooddescription_details

                foodname_details.setText(currentfoodList.getName());
                txtfoodprice_details.setText(currentfoodList.getPrice());
                txtfooddescription_details.setText(currentfoodList.getDescription());

                collapsingToolbarLayout.setTitle(currentfoodList.getName());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





}
