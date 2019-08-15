package com.example.anxietyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class PanicButton extends AppCompatActivity {

    //method to check if gps is enabled
    private void buildAlertMessageNoGps(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    public void sendSMS(String text){
        SmsManager sms = SmsManager.getDefault();



       try{
           sms.sendTextMessage("044" + Start.savedTelephone, null, text, null, null);

       }catch (Exception e ){

           Log.i("Failed sms", e.getMessage());

       }

    }

    LocationListener locationListener;
    LocationManager locationManager;
    TextView textView;
    TextView textView2;
    TextView resetTextview;
    Location lastknownlocation;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length >0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},1);

            }

        }else{

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            lastknownlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        }


    }


    //setting menu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout){
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            this.finishActivity(1);

        }else {

            if (item.getItemId()== R.id.resetcontats){

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("contactDetails");
                query.whereEqualTo("username", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size()>0){

                        ParseObject.deleteAllInBackground(objects, new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(PanicButton.this, "Contacts delete, Add new contacts.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),Start.class);
                                startActivity(intent);
                                try {
                                    this.finalize();
                                    Log.i("Finalized", "True");

                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        });

                    }
                    }
                });

            }

        }

        return super.onOptionsItemSelected(item);
    }




    @SuppressLint("MissingPermission")
    public void distressPressed(View view){

        //checking for gps
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }


        textView.setText("Don't Panic!");
        textView2.setText("Improbability Drive Activated!");

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {

            lastknownlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            List<Address> my_address =  geocoder.getFromLocation(lastknownlocation.getLatitude(), lastknownlocation.getLongitude(),1);
            String adress = my_address.get(0).getAddressLine(0);
            String text= "I think I might be having a panic attack, if you could call me to help me calm down, also, I'm at the current Address: "
                    + adress;
            sendSMS(text);
            Log.i("Sms succesfuly", "sent");


        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e ){

            Log.i("Exception", e.getMessage());

        }


        resetTextview.setVisibility(View.VISIBLE);



    }

    public void resetID(View view){

        textView.setText("   In case of panic...");
        textView2.setText("...Press me   ");
        resetTextview.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "Drive reset successful!", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        Log.i("one","true");
    }




    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic_button);
        textView= findViewById(R.id.upperTextview);
        textView2 = findViewById(R.id.downTextview);
        resetTextview = findViewById(R.id.reset);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

               locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                        3, locationListener);



            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (Build.VERSION.SDK_INT < 23){

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

        }else{

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                lastknownlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);



            }else {

                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);

            }

        }











    }
}
