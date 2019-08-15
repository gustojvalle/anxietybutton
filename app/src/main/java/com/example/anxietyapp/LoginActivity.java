package com.example.anxietyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    ParseUser parseUser ;
    EditText userView;
    EditText passView;
    String user;
    String password;

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        Log.i("one","true");
    }



    //checking if username and password have been typed.
    public boolean checkUsernameAndPassword(){

         user= String.valueOf(userView.getText());
         password= String.valueOf(userView.getText());

         if (user.isEmpty() || password.isEmpty()){

             Toast.makeText(this, "Please provide Username/Password", Toast.LENGTH_SHORT).show();
             return false;

         }else{

             Toast.makeText(this, "Thank You!", Toast.LENGTH_SHORT).show();
             parseUser= new ParseUser();
             parseUser.setUsername(user);
             parseUser.setPassword(password);

             return true;

         }

    }
//login method for login button
    public void login(View view){

        if (checkUsernameAndPassword()){

            parseUser.logInInBackground(user, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if(e==null) {
                        Intent intent = new Intent(getApplicationContext(), Start.class);
                        startActivity(intent);
                    }else {

                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }



    }

    //signup method for signup button
    public void signup(View view){

        if (checkUsernameAndPassword()){

            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null) {

                        //calling new activity Start to open
                        Intent intent = new Intent(getApplicationContext(), Start.class);
                        startActivity(intent);
                    }else {

                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE},1);
        //setting edit text for username and password
        userView = findViewById(R.id.username);
        passView = findViewById(R.id.password);
        if (ParseUser.getCurrentUser() != null){

            //calling new activity Start to open
            Intent intent = new Intent(getApplicationContext(), Start.class);
            startActivity(intent);

        }




    }
}
