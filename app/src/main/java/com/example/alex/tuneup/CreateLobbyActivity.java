package com.example.alex.tuneup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateLobbyActivity extends AppCompatActivity {
    EditText nameInput;
    Button select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);
        nameInput = findViewById(R.id.new_name_input);
        select = findViewById(R.id.submit_create_button);

    }


    private View.OnClickListener buttonListener = new View.OnClickListener() {
        //First check to see if player is null, then do test cases for the other things
        public void onClick(View v) {
            //TODO: Make this connect with server to create new lobby object there. As well, we should figure out if the lobby codes should
            //be randomly generated or input by user. For now, I am using a placeholder number (code)
            String name = nameInput.getText().toString(); //Name to be sent to server
            String code = "123456";
            Intent i = new Intent(getApplicationContext(), lobby_streamHidden.class);
            i.putExtra("lobbyKey", code);
            startActivity(i);
        }
    };
}