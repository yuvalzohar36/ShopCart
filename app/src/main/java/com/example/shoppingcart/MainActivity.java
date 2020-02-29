package com.example.shoppingcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private EditText Username;
    private EditText Password;
    private Button Login;
    private TextView Registerpage;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference reff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reff = FirebaseDatabase.getInstance().getReference().child("Users");

        Username = (EditText)findViewById(R.id.UsernameeditText); // define the username edit text
        Password = (EditText)findViewById(R.id.PasswordeditText); // define the password edit text
        Login = (Button)findViewById(R.id.LoginButton); // define the Login Button
        Registerpage = (TextView)findViewById(R.id.registerpage);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null){ /// check there might be some problem here
            finish();
            startActivity(new Intent(MainActivity.this, ApplicActivity.class)); /// build applicactivity
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Username.getText().toString(), Password.getText().toString());
            }
        });

        Registerpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }


        });


    }
     private void validate(String userName, String UserPassword){
        progressDialog.setMessage("We create your Shopping cart, just a moment...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName,UserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();

                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, ApplicActivity.class));

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
}}

