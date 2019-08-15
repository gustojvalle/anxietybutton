package com.example.anxietyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class Start extends AppCompatActivity {

    public void getContacts(){
        //Query to check if contact details are available.
        ParseQuery<ParseObject> query = new ParseQuery("contactDetails");
        query.whereEqualTo("username", ParseUser.getCurrentUser());
        Log.i("Username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.i("List", String.valueOf(objects));

                if (objects.size() > 0) {

                    Intent intent = new Intent(getApplicationContext(), PanicButton.class);
                    startActivity(intent);
                    savedEmail = String.valueOf(objects.get(0).get("email"));
                    savedTelephone=String.valueOf(objects.get(0).get("telephone"));
                    savedName =String.valueOf(objects.get(0).get("name"));

                }else {



                }
            }
        });


    }


    int i;
    EditText email ;
    EditText telephone;
    EditText name;
    ArrayList<String> info = new ArrayList<>();

    //setting contact details to be filled with parse server data.
    static String savedEmail;
    static String savedTelephone;
    static String savedName;
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        Log.i("one","true");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ImageButton button = findViewById(R.id.imageButton);
        email= findViewById(R.id.email);
        telephone = findViewById(R.id.phoneNumber);
        name = findViewById(R.id.name);
        i = 0;
        getContacts();




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (email.getText().toString().equals("") == false){

                    if (telephone.getText().toString().equals("") == false){

                      if (name.getText().toString().equals("") == false){


                          if (i==0) {
                              //Passing details to ParseObject so details can be saved in the server.
                              ParseObject object = new ParseObject("contactDetails");
                              object.put("email", String.valueOf(email.getText()));
                              object.put("telephone", String.valueOf(telephone.getText()));
                              object.put("name", String.valueOf(name.getText()));
                              object.put("username", ParseUser.getCurrentUser());
                              Log.i("Username to save", ParseUser.getCurrentUser().getUsername());

                              //Saving details on Parse server
                              object.saveInBackground(new SaveCallback() {
                                  @Override
                                  public void done(ParseException e) {
                                      if (e == null ){

                                          Toast.makeText(Start.this, "Details have been saved.", Toast.LENGTH_SHORT).show();
                                          Intent intent = new Intent(getApplicationContext(), PanicButton.class);
                                          startActivity(intent);
                                          getContacts();

                                      }else{

                                          Log.i("Failed: ", e.getMessage());

                                      }
                                  }
                              });




                          }
                          else {

                              Toast.makeText(Start.this, "List has already been filled", Toast.LENGTH_SHORT).show();

                          }

                      }  else {

                          Toast.makeText(Start.this, "Type a name", Toast.LENGTH_SHORT).show();

                      }

                    }else {

                        Toast.makeText(Start.this, "Enter a telephone number!", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(Start.this, "Enter an email!", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }
}
