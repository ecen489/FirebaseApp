package com.example.firebaseapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText emailText, passwordText;
    private Button createAccountButton, loginButton;
    private String email, password;

    private FirebaseDatabase fire;
    private DatabaseReference data;

    FirebaseAuth mAuth;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fire = FirebaseDatabase.getInstance();
        data = fire.getReference();
        mAuth = FirebaseAuth.getInstance();

        createAccountButton = findViewById(R.id.accountButton);
        loginButton = findViewById(R.id.loginButton);
        emailText = findViewById(R.id.email);
        passwordText  = findViewById(R.id.password);

        user = null;
    }

    public void createAccountClick(View view){
        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener
                (MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),
                            "New Account Created.",Toast.LENGTH_SHORT).show();
                    user = mAuth.getCurrentUser();
                    Intent pull = new Intent(MainActivity.this,
                            PullActivity.class);
                    startActivity(pull);
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Account Creation Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loginClick(View view){
        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener
                (MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),
                            "Login Successful.",Toast.LENGTH_SHORT).show();
                    user = mAuth.getCurrentUser();
                    Intent pull = new Intent(MainActivity.this,
                            PullActivity.class);
                    startActivity(pull);
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Incorrect Email/Password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
