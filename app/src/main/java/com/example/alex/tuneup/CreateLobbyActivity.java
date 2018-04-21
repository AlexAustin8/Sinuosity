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
            //TODO: Make this connect with server to create new lobby object there/ generate a code (just using async as a placeholder again)
            Async_Search a = new Async_Search(getApplicationContext());
            String code = a.execute(url, name);
            Intent i = new Intent(getApplicationContext(), lobby_streamHidden.class);
            i.putExtra("lobbyKey", code);
            startActivity(i);
        }
    };
}