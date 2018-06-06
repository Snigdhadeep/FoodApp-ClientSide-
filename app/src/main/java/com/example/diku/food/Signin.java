package com.example.diku.food;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diku.food.Common.Common;
import com.example.diku.food.Module.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signin extends AppCompatActivity {

    EditText et_ph,et_pass;
    TextView bt_signin;
    ImageView signinback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        et_ph=(EditText)findViewById(R.id.et_ph);
        et_pass=(EditText)findViewById(R.id.et_pass);
        bt_signin=(TextView)findViewById(R.id.bt_signin2);
        signinback=(ImageView)findViewById(R.id.signinback);

        signinback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Signin.this,GetStart.class);
                startActivity(intent);
            }
        });

//connect with firebase
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");
//click the button sign in
        bt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //calling firebase table (here i.e "User")
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // checking if number exists or not
                        if(dataSnapshot.child(et_ph.getText().toString()).exists()){

                            //if number exists in firebase database insert the child values (here 2 values are there Name & Password)
                            //into a model class ( here "User" )
                            User user = dataSnapshot.child(et_ph.getText().toString()).getValue(User.class);
                            //now check if password matheches with edittext
                            user.setPhone(et_ph.getText().toString());
                           if(user.getPassword().equals(et_pass.getText().toString()))
                           {

                               Intent homeintent=new Intent(Signin.this,Home.class);

                               //here we create a package named "Common" ,iside of that package we create astatic variable currentUser that holds the class
                               // "User" which is in the Package  "Module"
                               Common.currentUser=user;  //here we have inserted all the data collected from firebase child "user"(i.e phno)
                                                         //into the new variable "current user"
                               startActivity(homeintent);

                               finish();



                         }
    else{

        Toast.makeText(Signin.this, "try again ", Toast.LENGTH_SHORT).show();
    }
}

     else{

    Toast.makeText(Signin.this, "no such kind ph no exists ", Toast.LENGTH_SHORT).show();

}


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
