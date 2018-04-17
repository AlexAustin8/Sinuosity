package com.example.alex.tuneup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NonStreamLobbyActivity extends AppCompatActivity {
    Button queueAdd,viewQueue;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_stream_lobby);

        queueAdd = findViewById(R.id.create_button); //Using this object as a placeholder button until UI is properly defined
        viewQueue = findViewById(R.id.submit_create_button);
        queueAdd.setOnClickListener(lobbyButtonListener);
        viewQueue.setOnClickListener(lobbyButtonListener);
);

        Intent i = getIntent();
        key = i.getExtras().getString("lobbyKey");

        //TODO: Make a request to the sever using the lobbyKey with which we can populate all of the UI data
        //Once all of the inbound data is figured out, we just need to create an async to parse it and map it
        //to all proper values.

    }


    private View.OnClickListener lobbyButtonListener = new View.OnClickListener() {
        //First check to see if player is null, then do test cases for the other things
        public void onClick(View v) {
            Intent i;
            switch(v.getId()){
                case R.id.create_button: //Make sure to replace this as well as other one with proper id (addToQueue)
                    i = new Intent(getApplicationContext(), SearchActivity.class);
                    i.putExtra("lobbyKey", key);  //Operating under the assumption that we will need the key to add song to correct lobby
                    startActivity(i);
                    break;

                case R.id.submit_create_button: //view full queue
                    i = new Intent(getApplicationContext(), ViewQueueActivity.class);
                    i.putExtra("lobbyKey", key);  //Operating under the assumption that we will need the key to view correct lobby
                    startActivity(i);
            }
        }


    };



}
