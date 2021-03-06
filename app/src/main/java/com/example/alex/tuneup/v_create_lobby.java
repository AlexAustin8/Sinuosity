package com.example.alex.tuneup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class v_create_lobby extends AppCompatActivity {
    private EditText nameInput;
    private Button select;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Hides status and title bar --------------------------------------
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // -----------------------------------------------------------------

        setContentView(R.layout.create_lobby);


        LayoutInflater inflater = getLayoutInflater();
        getWindow().addContentView(inflater.inflate(R.layout.create_lobby_top, null), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));


        nameInput = findViewById(R.id.new_name_input);
        select = findViewById(R.id.submit_create_button);
        select.setOnClickListener(buttonListener);

        ImageView back = findViewById(R.id.bBackCreate);
        back.setOnClickListener(buttonListenerBack);

    }

    private View.OnClickListener buttonListenerBack = new View.OnClickListener() {
        //First check to see if player is null, then do test cases for the other things
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), v_home.class);
            startActivity(i);
        }
    };

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        //First check to see if player is null, then do test cases for the other things
        public void onClick(View v) {
            String name = nameInput.getText().toString(); //Name to be sent to server
            SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
            String userID = settings.getString("userID", "");
            RequestManager r = new RequestManager();

            //Although it does not yet, web_Create will return the lobby key, so afterwards the following line will be valid
            String code = r.web_lobbyCreate(name, userID);

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("lobbyID", code);
            editor.commit();

            Intent i = new Intent(getApplicationContext(), v_lobby.class);
            i.putExtra("lobbyKey", code);
            startActivity(i);
        }
    };
}