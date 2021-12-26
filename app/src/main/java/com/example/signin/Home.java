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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {

    private EditText user, pass, c_pass;
    private Button up, l_in;
    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

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

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("Users");

                String email=user.getText().toString().trim();
                String _pass=pass.getText().toString().trim();
                String con_pass=c_pass.getText().toString().trim();

                UserHelperClass helperClass=new UserHelperClass(email, _pass);

                reference.setValue(helperClass);

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

                if(con_pass.isEmpty()){
                    c_pass.setError("Confirmation password is required");
                    return;
                }

                if(!_pass.equals(con_pass)){
                    c_pass.setError("Password Do not Match!!");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, _pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(), Verify_Email.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Home.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}