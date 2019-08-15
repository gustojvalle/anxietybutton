package com.example.anxietyapp;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.Collections;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;

public class StarterApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .allEnabledTlsVersions()
                .allEnabledCipherSuites()
                .build();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        Parse.enableLocalDatastore(this);
        //initialization code
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .clientBuilder(builder)
                .applicationId("your app ID")
                .clientKey("your client key")
                .server("your server")
                .build()
        );
        Log.i("Parse server connected!", "true");




        //ParseUser.enableAutomaticUser();


        //Optionally enable public access
        //defaultACL.setPublicReadAccess(true);




    }
}
