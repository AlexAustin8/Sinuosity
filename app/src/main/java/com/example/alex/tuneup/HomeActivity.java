package com.example.alex.tuneup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    public Button create, submit;
    public EditText entry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        create = findViewById(R.id.create_button);
        submit = findViewById(R.id.submit_lobby_search);
        entry = findViewById(R.id.lobby_search);

        create.setOnClickListener(buttonListener);
        submit.setOnClickListener(buttonListener);

    }


    private View.OnClickListener buttonListener = new View.OnClickListener() {
        //First check to see if player is null, then do test cases for the other things
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.create_button:
                    //Todo: create the createLobbyActivity class and replace MainActivity.class with the proper file
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    break;

                case R.id.submit_lobby_search:
                    String key = entry.getText().toString();
                    String responseCode = "y";
                    /*TODO: In this case, we make a call to server searching for the key. If the response says that the server could not be found,
                      then we make a toast message to the user in inform them of the error. Else we launch a new intent to the lobby activity, with
                      info needed to connect to right lobby
                     */
                    if(responseCode.compareTo("n") ==0){
                        Toast.makeText(getApplicationContext(), "Could not find lobby, please enter new key", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent in = new Intent(getApplicationContext(), MainActivity.class);
                        in.putExtra("lobbyKey", key);
                        startActivity(i);
                    }

            }
        }


    };



}
