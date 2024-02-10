package com.example.aexpress.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aexpress.R;
import com.example.aexpress.adapters.ProductAdapter;
import com.example.aexpress.model.Product;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {
    TextInputEditText fullname, username, password, email;
    Button buttonSignUp;
    TextView txtLogin;
    ProgressBar progressBar;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);

        buttonSignUp = findViewById(R.id.buttonSignUp);
        txtLogin = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progress);

        preferences = getSharedPreferences("pref" , MODE_PRIVATE);
        editor = preferences.edit();




        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        if (preferences.getString("status" , "").equals("loggedin")){
            startActivity(new Intent(this , Login.class));
            finish();
        }
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName, userName, passWord, eMail;
                fullName = String.valueOf(fullname.getText());
                userName = String.valueOf(username.getText());
                passWord = String.valueOf(password.getText());
                eMail = String.valueOf(email.getText());

                if (!fullName.equals("") && !userName.equals("") && !passWord.equals("") && !eMail.equals("")  ) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[4];
                            field[0] = "fullname";
                            field[1] = "username";
                            field[2] = "password";
                            field[3] = "email";
                            //Creating array for data
                            String[] data = new String[4];
                            data[0] = fullName;
                            data[1] = userName;
                            data[2] = passWord;
                            data[3] = eMail;
                            PutData putData = new PutData("http://ecommerceapp.ir/loginregister/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (result.equals("Sign Up Success")){
                                        Toast.makeText(SignUp.this, result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext() , Login.class);
                                        startActivity(intent);
                                        editor.putString("status" , "loggedin").commit();
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(SignUp.this, result, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }else {
                    Toast.makeText(SignUp.this, "All fields required", Toast.LENGTH_SHORT).show();
                }


            }
        });



    }
}