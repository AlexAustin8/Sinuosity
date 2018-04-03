package com.example.alex.tuneup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Send a request to the server, and then parse the results into an
        //array of Track objects to put in the list. Then we can fill the list
        //Using the adapter


    }
}
