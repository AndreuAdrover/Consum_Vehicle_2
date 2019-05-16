package com.loker.consumvehicle.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.loker.consumvehicle.ViewModel.LoginViewModel;
import com.loker.consumvehicle.R;

public class SignUpActivity extends AppCompatActivity {

    public static final String NEW_USER_EMAIL = "com.loker.consumvehicle.activities.SignUpActivity.newUserEmail";
    public static final String NEW_USER_PASSWORD = "com.loker.consumvehicle.activities.SignUpActivity.newUserPassword";


    private String userName;
    private String password;

    private EditText etUserName,etPassword;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private LoginViewModel consumVehiclesVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //firebaseAuth = FirebaseAuth.getInstance();
        consumVehiclesVM = ViewModelProviders.of(this).get(LoginViewModel.class);

        etUserName =  findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        Button login = findViewById(R.id.register);

        progressDialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = etUserName.getText().toString();
                password = etPassword.getText().toString();
                regiter(userName,password);
                //Toast.makeText(getBaseContext(), userName+":"+password,Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void regiter(final String userName, final String password){

        progressDialog.setMessage("Creating new user pleas wait...");
        progressDialog.show();


        consumVehiclesVM.createNewUser(userName,password).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean success) {
                progressDialog.dismiss();
                if(success){
                    //go bakc to SignInActivity and logthe new user
                    Toast.makeText(SignUpActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                    Intent signInReply = new Intent();
                    signInReply.putExtra(NEW_USER_EMAIL,userName);
                    signInReply.putExtra(NEW_USER_PASSWORD,password);
                    setResult(RESULT_OK,signInReply);
                    finish();
                }else{
                    //display some message here
                    Toast.makeText(SignUpActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
