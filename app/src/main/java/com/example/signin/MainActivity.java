package com.example.signin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText user, pass;
    private Button button, up_button;
    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Sign in");

        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference();

         user=findViewById(R.id.username);
         pass=findViewById(R.id.pass);

         button=findViewById(R.id.button);
         up_button=findViewById(R.id.signup);

         button.setOnClickListener((View.OnClickListener) this);
         up_button.setOnClickListener((View.OnClickListener) this);
    }

    public void onClick(View view){
        if(view==up_button){
            startActivity(new Intent(MainActivity.this, Home.class));
        } else if(view==button){
            final String email=user.getText().toString();
            final String password=pass.getText().toString();

            if (email.isEmpty() || password.isEmpty() || !validEmail(email)){
                if(email.isEmpty()){
                    user.setError("Enter Email");
                    user.requestFocus();
                }else if(password.isEmpty() || password.length()<6){
                    pass.setError("Enter valid password");
                    pass.requestFocus();
                }else{
                    user.setError("Enter valid email");
                    user.requestFocus();
                }
            } else{
                auth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                boolean check=!task.getResult().getSignInMethods().isEmpty();
                                if(!check){
                                    Toast.makeText(getApplicationContext(), "Email is not Registered", Toast.LENGTH_SHORT).show();
                                } else{
                                    auth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if(task.isSuccessful()){
                                                        reference.child("user").child(auth.getUid()).child("mess_id").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(snapshot.exists()){
                                                                    startActivity(new Intent(getApplicationContext(), ManagerOrMember.class));
                                                                    finish();
                                                                } else{
                                                                    startActivity(new Intent(getApplicationContext(), Enter_mass.class));
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    } else{
                                                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        }
    }

    private boolean validEmail(String email) {
        String m="^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern= Pattern.compile(m, Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(email);

        return matcher.find();
    }

}
