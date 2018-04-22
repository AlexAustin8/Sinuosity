package com.example.alex.tuneup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateLobbyActivity extends AppCompatActivity {
    private EditText nameInput;
    private Button select;
    private String url = "https://anotherplaceholder.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);
        nameInput = findViewById(R.id.new_name_input);
        select = findViewById(R.id.submit_create_button);
        select.setOnClickListener(buttonListener);

    }


    private View.OnClickListener buttonListener = new View.OnClickListener() {
        //First check to see if player is null, then do test cases for the other things
        public void onClick(View v) {
            String name = nameInput.getText().toString(); //Name to be sent to server
            String id = "test"; //Using a dummy id for now
            RequestManager r = new RequestManager();
            r.web_lobbyCreate(name, id);
            String code = "123456";   //Just a sample until we figure out where the key will be generated
            Intent i = new Intent(getApplicationContext(), lobby_streamHidden.class);
            i.putExtra("lobbyKey", code);
            startActivity(i);
        }
    };
}