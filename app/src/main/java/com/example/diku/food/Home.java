package com.example.diku.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;

import com.example.diku.food.Common.Common;
import com.example.diku.food.Interface.ItemclickListener;
import com.example.diku.food.Module.Category;
import com.example.diku.food.Module.Order;
import com.example.diku.food.Service.ListenOrder;
import com.example.diku.food.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    TextView txt_fullname;
    RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(Home.this,Cart.class);
               startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseDatabase=FirebaseDatabase.getInstance();
        ref=firebaseDatabase.getReference("category");

        View viewheader=navigationView.getHeaderView(0);

        txt_fullname=(TextView)viewheader.findViewById(R.id.txt_fullname);
        txt_fullname.setText(Common.currentUser.getName());

        recyclerMenu=(RecyclerView)findViewById(R.id.recycler_menuhome);
        layoutManager=new LinearLayoutManager(this);
        recyclerMenu.setHasFixedSize(true);
        recyclerMenu.setLayoutManager(layoutManager);


        loadMenu();

        //Register service

        Intent intent = new Intent(Home.this, ListenOrder.class);
        startService(intent);



    }

    private void loadMenu() {

        adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.listitem,MenuViewHolder.class,ref) {
           @Override
           protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {

               viewHolder.txt_item_menu.setText(model.getName());

              // Toast.makeText(Home.this, ""+model.getImg(), Toast.LENGTH_SHORT).show();
               Picasso.with(getBaseContext())
                       .load(model.getImg())
                       .into(viewHolder.img_item_menu);



               viewHolder.setItemclickListener(new ItemclickListener() {
                   @Override
                   public void onclick(View view, int position, boolean isLongclick) {
                       Intent intent =new Intent(Home.this,FoodListclass.class);
                       intent.putExtra("foodkey",adapter.getRef(position).getKey());
                       Log.i("foodkey",adapter.getRef(position).getKey().toString());
                       startActivity(intent);
                   }
               });


           }
       };

       recyclerMenu.setAdapter(adapter);

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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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

            Intent intent=new Intent(this,Cart.class);
            startActivity(intent);

        } else if (id == R.id.nav_orders) {
            Intent intent=new Intent(this,OrderStatus.class);
            startActivity(intent);


        } else if (id == R.id.nav_logout) {

            Intent intent=new Intent(this,Signin.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
