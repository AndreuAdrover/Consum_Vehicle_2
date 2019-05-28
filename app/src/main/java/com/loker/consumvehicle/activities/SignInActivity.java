package com.loker.consumvehicle.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loker.consumvehicle.ViewModel.LoginViewModel;
import com.loker.consumvehicle.R;

public class SignInActivity extends AppCompatActivity {

    private String userName;
    private String password;

    public static final int REGITER_REQUEST = 1;

    private EditText etUserName,etPassword;

    private LoginViewModel consumVehiclesVM;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //firebaseAuth = FirebaseAuth.getInstance();
        consumVehiclesVM = ViewModelProviders.of(this).get(LoginViewModel.class);
        progressDialog = new ProgressDialog(this);

        etUserName =  findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        Button login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = etUserName.getText().toString();
                password = etPassword.getText().toString();
                logIn(userName, password);
            }
        });

        TextView register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),SignUpActivity.class);
                startActivityForResult(intent, REGITER_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REGITER_REQUEST){
            if(resultCode == RESULT_OK){
                //login with the new user
                if(data != null){
                    userName = data.getStringExtra(SignUpActivity.NEW_USER_EMAIL);
                    password = data.getStringExtra(SignUpActivity.NEW_USER_PASSWORD);

                    logIn(userName,password);
                }
            }
        }
    }

    private void logIn(String name, String password){
        //Toast.makeText(getBaseContext(), userName+":"+password,Toast.LENGTH_SHORT).show();
        progressDialog.setMessage("Loggig please wait..");
        progressDialog.show();
        //logging in the user
        consumVehiclesVM.login(name, password).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean success) {
                progressDialog.dismiss();
                if(success){
                    //start the MainActivity
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"User or password incorrect",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
