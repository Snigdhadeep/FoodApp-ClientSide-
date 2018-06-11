package com.example.diku.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.diku.food.AutofitGridLayoutManeger.AutogridLayoutManeger;
import com.example.diku.food.Common.Common;
import com.example.diku.food.Interface.ItemclickListener;
import com.example.diku.food.Module.CarouselList;
import com.example.diku.food.Module.Category;
import com.example.diku.food.Module.FoodList;
import com.example.diku.food.Module.Order;
import com.example.diku.food.Module.User;
import com.example.diku.food.Service.ListenOrder;
import com.example.diku.food.ViewHolder.CarouselAdapter;
import com.example.diku.food.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import java.util.HashMap;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BaseSliderView.OnSliderClickListener ,ViewPagerEx.OnPageChangeListener{


    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    DatabaseReference refCaraosel;
    TextView txt_fullname;
    RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;
    HashMap<String, String> HashMapForURL ;

    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    FirebaseRecyclerAdapter<CarouselList,CarouselAdapter> adapter_carousel;

    SliderLayout sliderLayout ;
    int i=1;
    CarouselList carouselList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseDatabase=FirebaseDatabase.getInstance();
        ref=firebaseDatabase.getReference("category");

        refCaraosel=firebaseDatabase.getReference("carousel");

        //carouselView

        sliderLayout = (SliderLayout)findViewById(R.id.slider);





        refCaraosel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 carouselList = dataSnapshot.child("img").getValue(CarouselList.class);


                 HashMapForURL = new HashMap<String,String>();
                String image1=carouselList.getImage1();
                HashMapForURL.put(carouselList.getName1(),carouselList.getImage1());

                HashMapForURL.put(carouselList.getName2(),carouselList.getImage2());
                HashMapForURL.put(carouselList.getName3(),carouselList.getImage3());
                HashMapForURL.put(carouselList.getName4(),carouselList.getImage4());

                Log.i("wow",image1);


                for(String name : HashMapForURL.keySet()){

                    TextSliderView textSliderView = new TextSliderView(Home.this);

                    textSliderView
                            .description(name)
                            .image(HashMapForURL.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(Home.this);

                    textSliderView.bundle(new Bundle());

                    textSliderView.getBundle()
                            .putString("extra",name);

                    sliderLayout.addSlider(textSliderView);
                }
                sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);

                sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

                sliderLayout.setCustomAnimation(new DescriptionAnimation());

                sliderLayout.setDuration(3000);

                sliderLayout.addOnPageChangeListener(Home.this);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        View viewheader=navigationView.getHeaderView(0);

        txt_fullname=(TextView)viewheader.findViewById(R.id.txt_fullname);
        txt_fullname.setText(Common.currentUser.getName());

        recyclerMenu=(RecyclerView)findViewById(R.id.recycler_menuhome);
        //layoutManager=new GridLayoutManager(this,2);
        AutogridLayoutManeger layoutManager = new AutogridLayoutManeger(this, 200);
        recyclerMenu.setHasFixedSize(true);
        recyclerMenu.setLayoutManager(layoutManager);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerMenu.setLayoutManager(manager);


        loadMenu();
       // AddImagesUrlOnline();
       // callurl();

        //Register service

        Intent intent = new Intent(Home.this, ListenOrder.class);
        startService(intent);




    }

    private void callurl() {

        //Call this method to stop automatic sliding.
        //sliderLayout.stopAutoCycle();

        for(String name : HashMapForURL.keySet()){

            TextSliderView textSliderView = new TextSliderView(Home.this);

            textSliderView
                    .description(name)
                    .image(HashMapForURL.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());

            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(3000);

        sliderLayout.addOnPageChangeListener(Home.this);
    }


    public void AddImagesUrlOnline(){


        HashMapForURL = new HashMap<String, String>();

       // HashMapForURL.put("CupCake", String.valueOf(imgurl));
        HashMapForURL.put("Donut", "https://thumb1.shutterstock.com/display_pic_with_logo/564163/340505519/stock-photo-cacciatori-chicken-with-mushrooms-and-black-olives-in-a-frying-pan-horizontal-top-view-340505519.jpg");
        HashMapForURL.put("Eclair", "http://androidblog.esy.es/images/eclair-3.png");
        HashMapForURL.put("Froyo", "http://androidblog.esy.es/images/froyo-4.png");
        HashMapForURL.put("GingerBread", "http://androidblog.esy.es/images/gingerbread-5.png");
    }

    private void loadMCarousel() {

        adapter_carousel=new FirebaseRecyclerAdapter<CarouselList, CarouselAdapter>(
                CarouselList.class,
                R.layout.carouselview,
                CarouselAdapter.class,
                refCaraosel
        ) {
            @Override
            protected void populateViewHolder(CarouselAdapter viewHolder, CarouselList model, int position) {







            }
        };


    }





    private void loadMenu() {

        adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, R.layout.listitem,MenuViewHolder.class,ref) {
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
                       Intent intent =new Intent(Home.this,FoodListClass1.class);
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
        if (id == R.id.action_cart) {
            Intent intent=new Intent(Home.this,Cart.class);
            startActivity(intent);
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

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
