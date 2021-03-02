package com.example.e_campusguide;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity implements ValueEventListener {

    private TextView id,name,email,address,phone,program,session;
    private FirebaseAuth mAuth;
    private FirebaseDatabase dbInstance;
    private DatabaseReference dbReference;
    private String uid;

    private String stId,stName,stProg,sess,add,stEmail,stPhn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        id=(TextView) findViewById(R.id.stid);
        name=(TextView) findViewById(R.id.stname);
        email=(TextView) findViewById(R.id.email);
        address=(TextView) findViewById(R.id.address);
        phone=(TextView) findViewById(R.id.phn);
        program=(TextView) findViewById(R.id.program);
        session=(TextView) findViewById(R.id.session);

        mAuth =FirebaseAuth.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbReference = dbInstance.getInstance().getReference().getRoot().child("Student").child(uid);
        dbReference.addValueEventListener(this);

    }

    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        stId = dataSnapshot.child("studentid").getValue().toString();
        stName = dataSnapshot.child("name").getValue().toString();
        add = dataSnapshot.child("address").getValue().toString();
        stEmail = dataSnapshot.child("email").getValue().toString();
        stPhn = dataSnapshot.child("phone").getValue().toString();
        stProg = dataSnapshot.child("program").getValue().toString();
        sess = dataSnapshot.child("session").getValue().toString();

        id.setText(stId);
        name.setText(stName);
        email.setText(stEmail);
        address.setText(add);
        phone.setText(stPhn);
        program.setText(stProg);
        session.setText(sess);

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

}
