package com.example.e_campusguide;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private  EditText name;
    private  EditText add;
    private  EditText email;
    private  EditText pass ;
    private  EditText rpass ;
    private  EditText phone;
    private  EditText id ;
    private  EditText start;
    private  EditText end;
    private  String session,program;

    private FirebaseDatabase databaseReference;


    private String [] programs = {"BBA","BCS","MBA","Bs Social Sciences","Bs Software Engineering","MS Computer Science"};
    private Spinner spin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance();
        name = (EditText) findViewById(R.id.input_name);
        add= (EditText) findViewById(R.id.input_address);
        email = (EditText) findViewById(R.id.input_email);
        pass = (EditText) findViewById(R.id.input_password);
        rpass = (EditText) findViewById(R.id.input_reEnterPassword);
        phone = (EditText) findViewById(R.id.input_mobile);
        id = (EditText) findViewById(R.id.sid);
        start = (EditText) findViewById(R.id.startSession);
        end = (EditText) findViewById(R.id.endSession);

        spin= (Spinner) findViewById(R.id.program);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa= new ArrayAdapter(this,android.R.layout.simple_spinner_item,programs);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);



    }




    public void signUpBtn(View view)
    {
        session=start.getText()+"-"+end.getText();
        program=spin.getSelectedItem().toString();

        if(TextUtils.isEmpty(id.getText().toString()))
        {
            id.setError("Provide Student ID");
        }
        else if(TextUtils.isEmpty(name.getText().toString()))
        {
            name.setError("Enter Name");
        }
        else if(TextUtils.isEmpty(email.getText().toString()))
        {
            email.setError("Enter Email");
        }
        else if(TextUtils.isEmpty(add.getText().toString()))
        {
            add.setError("Address is required");
        }
        else if(TextUtils.isEmpty(phone.getText().toString()))
        {
            phone.setError("Provide Phone NO.");
        }
        else if(TextUtils.isEmpty(pass.getText().toString()))
        {
            pass.setError("Enter Password");
        }
        else if(TextUtils.isEmpty(rpass.getText().toString()))
        {
            rpass.setError("Re-enter Password");
        }

        else if(pass.getText().toString().length()>=6){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            if (pass.getText().toString().equals(rpass.getText().toString())) {
                                User user = new User(id.getText().toString(),
                                        name.getText().toString(),
                                        program,
                                        session,
                                        add.getText().toString(),
                                        email.getText().toString(),
                                        phone.getText().toString());


                                FirebaseUser u = mAuth.getCurrentUser();
                                databaseReference.getReference("Student").child(u.getUid()).setValue(user);
                                Toast.makeText(SignUp.this, "User Created Successfully", Toast.LENGTH_LONG).show();

                                id.setText("");
                                name.setText("");
                                email.setText("");
                                add.setText("");
                                phone.setText("");
                                pass.setText("");
                                rpass.setText("");
                                start.setText("");
                                end.setText("");

                            }
                            else {
                                Toast.makeText(SignUp.this, "Password Doesn't Match", Toast.LENGTH_LONG).show();

                            }



                        }
                        else{
                            Toast.makeText(SignUp.this, "Failed to Create Account", Toast.LENGTH_LONG).show();

                        }

                        }


                });}
        else {
            Toast.makeText(SignUp.this, "Password is too short", Toast.LENGTH_LONG).show();
        }



    }

    public void logInbtn2(View view) {

        Intent ij = new Intent(this,SignIn.class);
        startActivity(ij);
        finish();
    }

    public void passClicked(View view) {

        if(TextUtils.isEmpty(pass.getText().toString()) || pass.length() < 6 || pass.length()>15)
        {
            pass.setError("Password must be 6-15 characters ");
            return;
        }

    }

    public void rpassClicked(View view) {
        if(TextUtils.isEmpty(rpass.getText().toString()) || rpass.length() < 6 || rpass.length()>15)
        {
            rpass.setError("Password must be 6-15 characters ");
            return;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //  Toast.makeText(getApplicationContext(),programs[position],Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
}
