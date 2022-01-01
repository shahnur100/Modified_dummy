package com.example.signin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    private EditText user, pass, c_pass;
    private Button up, l_in;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.setTitle("Sign Up");
        mAuth = FirebaseAuth.getInstance();

        user=findViewById(R.id.username);
        pass=findViewById(R.id.pass);
        c_pass=findViewById(R.id.confirm_pass);

        up=(Button) findViewById(R.id.sign_uP);
        l_in=findViewById(R.id.already_in);


        l_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,MainActivity.class));
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister();
            }
        });

    }




    private void userRegister() {
        String email=user.getText().toString().trim();
        String _pass=pass.getText().toString().trim();

        if(email.isEmpty())
        {
            user.setError("Enter an email address");
            user.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            user.setError("Enter a valid email address");
            user.requestFocus();
            return;
        }


        if(_pass.isEmpty()){
            pass.setError("Enter a password");
            pass.requestFocus();
            return;
        }

        if(_pass.length()<6){
            pass.setError("Minimum length of a password should be 6");
            pass.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, _pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user=mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Sign Up is successful", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Sign Up is unsuccessful", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}