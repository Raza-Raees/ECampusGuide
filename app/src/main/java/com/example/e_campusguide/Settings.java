package com.example.e_campusguide;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ValueEventListener {


    private DrawerLayout mdrawerlayout;
    private Button b;
    private TextView email;
    private TextView name;
    private String id;
    private String username;
    private NavigationView nv;
    private FirebaseAuth mAuth;

    private FirebaseDatabase dbInstance;
    private DatabaseReference dbReference;
    private String uid;

    private String stId,stName,stprog,session,address,stEmail,stP;

    private EditText n1,a1,p1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mdrawerlayout = (DrawerLayout) findViewById(R.id.LoggedMainMenu);
        b=(Button) findViewById(R.id.btn);

        n1 = (EditText) findViewById(R.id.name);
        a1 = (EditText) findViewById(R.id.address);
        p1 = (EditText) findViewById(R.id.phone);

        NavigationView nv = (NavigationView) findViewById(R.id.nv2);
        nv.setNavigationItemSelectedListener(this);

        mAuth =FirebaseAuth.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbReference = dbInstance.getInstance().getReference().getRoot().child("Student").child(uid);
        dbReference.addValueEventListener(this);




        View header = nv.getHeaderView(0);
        email = (TextView) header.findViewById(R.id.id);
        name = (TextView) header.findViewById(R.id.name);





        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdrawerlayout.openDrawer(Gravity.LEFT );
            }
        });





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id == R.id.chat)
        {


            mdrawerlayout.closeDrawers();

            finish();

        }



        if(id == R.id.profile)
        {
            Intent i=new Intent(Settings.this,UserProfile.class);
            startActivity(i);

        }

        if(id == R.id.settings)
        {

            mdrawerlayout.closeDrawers();

        }

        if(id == R.id.back)
        {   super.onBackPressed();
            mdrawerlayout.closeDrawers();



        }


        return false;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        stId = dataSnapshot.child("studentid").getValue().toString();
        stName = dataSnapshot.child("name").getValue().toString();
        address = dataSnapshot.child("address").getValue().toString();
        stEmail = dataSnapshot.child("email").getValue().toString();
        stP = dataSnapshot.child("phone").getValue().toString();
        stprog= dataSnapshot.child("program").getValue().toString();
        session= dataSnapshot.child("session").getValue().toString();

        email.setText(stEmail);
        name.setText(stName);

        n1.setText(stName);
        a1.setText(address);
        p1.setText(stP);




    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public void change(View view) {

        String a=n1.getText().toString(), b=a1.getText().toString(), c=p1.getText().toString();

        if(TextUtils.isEmpty(a))
        {
            n1.setError("Name Required");
        }

        else if(TextUtils.isEmpty(b))
        {
            a1.setError("Address Required");
        }

        else if(TextUtils.isEmpty(c))
        {
            p1.setError("Phone no. Required");
        }


        else {
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Student").child(uid);
            User user= new User(stId,a,stprog,session,b,stEmail,c);
            databaseReference.setValue(user);
            Toast.makeText(this, "User Updated", Toast.LENGTH_LONG).show();
        }
    }
}
