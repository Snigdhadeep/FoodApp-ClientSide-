package com.example.diku.food;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.diku.food.DataBase.Database;
import com.example.diku.food.Module.FoodList;
import com.example.diku.food.Module.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetails extends AppCompatActivity {


    TextView foodname_details,txtfoodprice_details,txtfooddescription_details;
    ImageView img_fooddetails;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ElegantNumberButton btn_elegant;


    String foodlistId="";
    FoodList currentfoodList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference fooddetails_ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseDatabase=FirebaseDatabase.getInstance();
        fooddetails_ref=firebaseDatabase.getReference("Foods");

      //  elegantNumberButton=(ElegantNumberButton)findViewById(R.id.btn_elegant);

        foodname_details=(TextView)findViewById(R.id.foodname_details);
        txtfoodprice_details=(TextView)findViewById(R.id.txtfoodprice_details);
        txtfooddescription_details=(TextView)findViewById(R.id.txtfooddescription_details);

        btn_elegant=(ElegantNumberButton)findViewById(R.id.btn_elegant);

        img_fooddetails=(ImageView)findViewById(R.id.img_fooddetails);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);

        FloatingActionButton fabAddtocart = (FloatingActionButton) findViewById(R.id.fab_cart_details);
        fabAddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Database(getBaseContext()).addToCart(new Order(

                        foodlistId,
                        currentfoodList.getName(),

                        btn_elegant.getNumber(),
                        currentfoodList.getPrice(),
                        currentfoodList.getDiscount()

                ));


                Toast.makeText(FoodDetails.this, "added to cart "+foodlistId, Toast.LENGTH_SHORT).show();
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
