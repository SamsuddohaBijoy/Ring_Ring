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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SigninActivity extends AppCompatActivity {

    TextInputEditText  user_email, user_password;

    AppCompatButton signin_btn;

    FirebaseAuth firebaseAuth;

    TextView no_have_text;

    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        firebaseAuth = FirebaseAuth.getInstance();



        dialog = new ProgressDialog(SigninActivity.this);

        dialog.setMessage("Please wait");

        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        signin_btn = findViewById(R.id.signin_btn);
        no_have_text = findViewById(R.id.no_have_text);

        no_have_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SigninActivity.this,RegisterActivity.class));
                finish();

            }
        });
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_email.getText().toString().trim();
                String password = user_password.getText().toString().trim();

                if (email.isEmpty()){
                    ShowAlert("Email filed can not be empty!");
                }
                else if (password.isEmpty()){
                    ShowAlert("password filed can not be empty!");
                }
                else if (password.length()<6){
                    ShowAlert("password should be more then 6");
                }else {

                    dialog.show();

                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()){



                                Toast.makeText(SigninActivity.this,"Sign in successfully",Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(SigninActivity.this,MainActivity.class));
                                 finish();
                            }
                        }
                    });
                }
            }
        });
    }
    private void ShowAlert(String s){

        AlertDialog.Builder builder = new AlertDialog.Builder(SigninActivity.this);

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