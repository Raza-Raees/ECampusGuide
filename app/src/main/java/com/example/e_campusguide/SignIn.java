package com.example.e_campusguide;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {



    private FirebaseAuth mAuth;
    private EditText email ;
    private EditText pass ;
    private String e;
    private String p;
    private String n;



    private String keys;

    private DatabaseReference mDatabase;

    private DatabaseReference reff;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth=FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.input_email);
        pass = (EditText) findViewById(R.id.input_password);


    }
    public void logInBtn(View view) {


        e=email.getText().toString();
        p=pass.getText().toString();


        if(TextUtils.isEmpty(e))
        {
            email.setError("Email Field is Empty");
        }
        else if(TextUtils.isEmpty(p))
        {
            pass.setError("Password Field is Empty");
        }

        else  if(!TextUtils.isEmpty(e)||!TextUtils.isEmpty(p)){
            mAuth.signInWithEmailAndPassword(e, p)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(SignIn.this
                                        , "Sign-in Successful", Toast.LENGTH_LONG).show();


                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                Intent i = new Intent(SignIn.this, MainMenu2.class);
                                if (user == null) throw new AssertionError();


                                if (user != null) {

                                    i.putExtra("email", e);
                                    i.putExtra("name", keys);
                                    finish();
                                    startActivity(i);

                                }


                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Username or password is Incorrect",Toast.LENGTH_LONG).show();

                            }

                        }
                    });
        }
    }








    public void signUpBtn(View view)
    {

        Intent ij = new Intent(this,SignUp.class);
        startActivity(ij);
        finish();

    }

}
