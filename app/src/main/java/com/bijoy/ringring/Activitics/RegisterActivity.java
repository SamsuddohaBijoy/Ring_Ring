package com.bijoy.ringring.Activitics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import com.bijoy.ringring.R;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText user_name, user_number, user_email, user_password;

    AppCompatButton register_btn;

    TextView have_text;

    FirebaseAuth firebaseAuth;

    ProgressDialog dialog;

    DatabaseReference databaseReference;

    FirebaseUser firebaseUser;

    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();


        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        dialog = new ProgressDialog(RegisterActivity.this);

        dialog.setMessage("Please wait");


        user_name = findViewById(R.id.user_name);
        user_number = findViewById(R.id.user_number);
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        register_btn = findViewById(R.id.register_btn);
        have_text = findViewById(R.id.have_text);

        have_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this, SigninActivity.class));
                finish();

            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @NonNull
            @Override
            public String toString() {
                return super.toString();
            }

            @Override
            public void onClick(View v) {

                String name = user_name.getText().toString().trim();
                String number = user_number.getText().toString().trim();
                String email = user_email.getText().toString().trim();
                String password = user_password.getText().toString().trim();

                if (name.isEmpty()) {

                    ShowAlert("Name filed can not be empty!");

                }else if (number.isEmpty()){
                    ShowAlert("Number filed can not be empty!");
                }
                else if (email.isEmpty()){
                    ShowAlert("Email filed can not be empty!");
                }
                else if (password.isEmpty()){
                    ShowAlert("password filed can not be empty!");
                }
                else if (password.length()<6){
                    ShowAlert("password should be more then 6");
                }
                else {
                    dialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            firebaseAuth = FirebaseAuth.getInstance();
                            firebaseUser = firebaseAuth.getCurrentUser();

                            if (firebaseUser!=null){
                             currentUser =   firebaseUser.getUid();
                            }

                            dialog.dismiss();

                            if (task.isSuccessful()) {

                                HashMap<String,String> userMap = new HashMap<>();
                                userMap.put("UserName",name);
                                userMap.put("UserNumber",number);
                                userMap.put("UserEmail",email);
                                userMap.put("UserPassword",password);
                                userMap.put("userId",currentUser);

                                databaseReference.child(currentUser).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                            Toast.makeText(RegisterActivity.this, "Create Account Succeshfully", Toast.LENGTH_SHORT).show();

                                            startActivity(new Intent(RegisterActivity.this,SigninActivity.class));

                                            finish();
                                        }
                                    }
                                });



                            } else {
                                String err = task.getException().getLocalizedMessage();

                                ShowAlert(err);

                            }

                        }
                    });
                }




            }
        });
    }

    private void ShowAlert(String s){

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

        builder.setTitle("Error");
        builder.setMessage(s);
        builder.setIcon(R.drawable.ic_baseline_new_releases_24);

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
}