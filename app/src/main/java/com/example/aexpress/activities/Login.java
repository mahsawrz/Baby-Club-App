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
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login extends AppCompatActivity {
    TextInputEditText username, password;
    Button buttonLogin;
    TextView txtSignUp;
    ProgressBar progressBar;

    SharedPreferences preferences1;
    SharedPreferences.Editor editor1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        buttonLogin = findViewById(R.id.buttonLogin);
        txtSignUp = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progress);

        preferences1 = getSharedPreferences("pref" , MODE_PRIVATE);
        editor1 = preferences1.edit();





        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        if (preferences1.getString("stattus" , "").equals("logggedin")){
            startActivity(new Intent(this , MainActivity.class));
            finish();
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName, passWord;
                userName = String.valueOf(username.getText());
                passWord = String.valueOf(password.getText());


                if (!userName.equals("") && !passWord.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = userName;
                            data[1] = passWord;
                            PutData putData = new PutData("http://ecommerceapp.ir/loginregister/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (result.equals("Login Success")){
                                        Toast.makeText(Login.this, result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                                        startActivity(intent);
                                        editor1.putString("stattus" , "logggedin").commit();
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(Login.this, result, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }else {
                    Toast.makeText(Login.this, "All fields required", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}