package com.example.diku.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.diku.food.Common.Common;
import com.example.diku.food.Interface.ItemclickListener;
import com.example.diku.food.Module.FoodList;
import com.example.diku.food.Module.User;
import com.example.diku.food.ViewHolder.FoodViewHolder2;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FoodListClass1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String categoryid="";

    FirebaseDatabase firebaseDatabase;
    public DatabaseReference fooditems;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<FoodList,FoodViewHolder2> adapter;
    //search functionality

    FirebaseRecyclerAdapter<FoodList,FoodViewHolder2> search_adapter;
    List<String> suggestList=new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_class1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);



        firebaseDatabase=FirebaseDatabase.getInstance();
        fooditems=firebaseDatabase.getReference("Foods");





        recyclerView=(RecyclerView)findViewById(R.id.rv_foodlist);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //search
        materialSearchBar=(MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("");
        loadSuggest();

        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (suggestList.isEmpty())
                Log.i("search2","suggestList is empty ");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("search2","enterd into ontextChanged");

                List<String> suggest=new ArrayList<String>();
                for(String search:suggestList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);


                }

                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when search bar is closed
                //restore original adapter
                if(!enabled){
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                //when search is finished
                //show result of search adapter
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });


        if(getIntent()!=null) {

            categoryid = getIntent().getStringExtra("foodkey");
        }

        if(!categoryid.isEmpty() && categoryid!= null){

            loadlist(categoryid);
            //Toast.makeText(this, "function worked"+categoryid, Toast.LENGTH_SHORT).show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void startSearch(CharSequence text) {


        search_adapter=new FirebaseRecyclerAdapter<FoodList, FoodViewHolder2>(
                FoodList.class,
                R.layout.foodlist_item,
                FoodViewHolder2.class,
                fooditems.orderByChild("name").equalTo(text.toString())

        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder2 viewHolder, FoodList model, int position) {



                Log.i("getprice14",model.getPrice());
                Log.i("getdiscount14",model.getDiscount());


                double price=Double.parseDouble(model.getPrice());
                double discount=Double.parseDouble(model.getDiscount());
               double discounted_money=(price-(price*(discount/100)));
                Log.i("gettotal14",String.valueOf(discounted_money));


                viewHolder.txt_fooditem_name.setText(model.getName());
                viewHolder.txt_fooditem_price.setText(model.getPrice());
                viewHolder.txt_fooditem_discount.setText(model.getDiscount());
                viewHolder.txt_discountedprice.setText(String.valueOf(roundTwoDecimals(discounted_money)));
                viewHolder.txt_fooditem_unit.setText(model.getUnit());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.img_item_food);


                final FoodList local=model;
                viewHolder.setItemclickListener(new ItemclickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongclick) {
                        Intent intent=new Intent(FoodListClass1.this,FoodDetails.class);
                        intent.putExtra("foodlistId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }
        };

        recyclerView.setAdapter(search_adapter);

    }


    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("##00.0#");
        return Double.valueOf(twoDForm.format(d));
    }

    private void loadSuggest() {

        Toast.makeText(FoodListClass1.this, "loadsuggest", Toast.LENGTH_SHORT).show();
       fooditems.orderByChild("menuid").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               Toast.makeText(FoodListClass1.this, ""+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();


               for(DataSnapshot postSnapshot:dataSnapshot.getChildren()) {

                   FoodList item = postSnapshot.getValue(FoodList.class);

                   suggestList.add(item.getName());
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });


       /* fooditems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
            FoodList post = postSnapshot.getValue(FoodList.class);
                    Log.e("Get Data", post.<YourMethod>());
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });*/
/*
        fooditems.orderByChild("menuid").equalTo(categoryid).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        for(DataSnapshot postSnapshot:dataSnapshot.getChildren()) {

            FoodList item = postSnapshot.getValue(FoodList.class);
            Toast.makeText(FoodListClass1.this, "hello"+item.getName(), Toast.LENGTH_SHORT).show();
            suggestList.add(item.getName());
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
*/

    }

    public void loadlist(String categoryid) {


        adapter=new FirebaseRecyclerAdapter<FoodList, FoodViewHolder2>(
                FoodList.class,
                R.layout.foodlist_item,
                FoodViewHolder2.class,
                fooditems.orderByChild("menuid").equalTo(categoryid)

        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder2 viewHolder, final FoodList model, int position) {


                Log.i("getprice14",model.getPrice());
                Log.i("getdiscount14",model.getDiscount());


                double price=Double.parseDouble(model.getPrice());
                double discount=Double.parseDouble(model.getDiscount());
                final double discounted_money=(price-(price*(discount/100)));
                Log.i("gettotal14",String.valueOf(discounted_money));

                viewHolder.txt_fooditem_name.setText(model.getName());
                viewHolder.txt_fooditem_price.setText(model.getPrice());
                viewHolder.txt_fooditem_discount.setText(model.getDiscount());
                viewHolder.txt_discountedprice.setText(String.valueOf(roundTwoDecimals(discounted_money)));
                viewHolder.txt_fooditem_unit.setText(model.getUnit());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.img_item_food);





                final FoodList local=model;
                viewHolder.setItemclickListener(new ItemclickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongclick) {
                        Intent intent=new Intent(FoodListClass1.this,FoodDetails.class);
                        intent.putExtra("foodlistId", adapter.getRef(position).getKey());
                        intent.putExtra("foodlistdiscount", local.getDiscount());
                        intent.putExtra("foodlistprice", String.valueOf(roundTwoDecimals(discounted_money)));
                        startActivity(intent);
                    }
                });


            }
        };




        recyclerView.setAdapter(adapter);





    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.food_list_class1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_search) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {

            Intent intent = new Intent(this, Cart.class);
            startActivity(intent);

        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(this, OrderStatus.class);
            startActivity(intent);


        } else if (id == R.id.nav_logout) {

            Intent intent = new Intent(this, Signin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
