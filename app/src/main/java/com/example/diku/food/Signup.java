package com.example.diku.food;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diku.food.Module.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {
    EditText et_phup,et_passup,et_name;
    Button bt_signup2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        et_phup=(EditText)findViewById(R.id.et_phup);
        et_passup=(EditText)findViewById(R.id.et_passup);
        et_name=(EditText)findViewById(R.id.et_name);

        bt_signup2=(Button)findViewById(R.id.bt_signup2);

        //accessing firebase database
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        //catch the table in database
        final DatabaseReference table=database.getReference("User");


        bt_signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog dialog=new ProgressDialog(Signup.this);
                dialog.show();

               table.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       if(dataSnapshot.child(et_phup.getText().toString()).exists()){

                           dialog.dismiss();
                           Toast.makeText(Signup.this, "ph no already resgistered", Toast.LENGTH_SHORT).show();

                       }

                       else{

                           dialog.dismiss();
                           User user=new User(et_name.getText().toString(),et_passup.getText().toString());
                           table.child(et_phup.getText().toString()).setValue(user);
                           Toast.makeText(Signup.this, "registerd your ph no.", Toast.LENGTH_SHORT).show();
                           finish();

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
