package com.example.signin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Verify_Email extends AppCompatActivity {

    TextView verify_msg;
    Button verify_email;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        auth=FirebaseAuth.getInstance();

        Button logout=findViewById(R.id.button3);
        verify_msg=findViewById(R.id.txt);
        verify_email=findViewById(R.id.button2);

        if(!auth.getCurrentUser().isEmailVerified()){
            verify_email.setVisibility(View.VISIBLE);
            verify_msg.setVisibility(View.VISIBLE);
        }

        verify_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Verify_Email.this, "Verification mail is sent", Toast.LENGTH_SHORT).show();
                        verify_email.setVisibility(View.GONE);
                        verify_msg.setVisibility(View.GONE);
                    }
                });
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}