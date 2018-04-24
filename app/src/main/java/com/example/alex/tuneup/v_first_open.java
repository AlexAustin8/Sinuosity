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

public class v_first_open extends AppCompatActivity {
    private EditText nameInput;
    private Button select;

    private String url = "https://anotherplaceholder.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Hides status and title bar --------------------------------------
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // -----------------------------------------------------------------

        setContentView(R.layout.first_open);


        LayoutInflater inflater = getLayoutInflater();
        getWindow().addContentView(inflater.inflate(R.layout.first_open_top, null), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));


        nameInput = findViewById(R.id.new_display_name_input);
        select = findViewById(R.id.submit_name);
        select.setOnClickListener(buttonListener);



    }

    // Sends a request to the server with a display name to create a new user. Server responds with unique userID and it is saved in sharedPreferences along with displayName
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        //First check to see if player is null, then do test cases for the other things
        public void onClick(View v) {
            SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
            String displayName = nameInput.getText().toString();

            RequestManager rm = new RequestManager();
            String userID = rm.web_settingsCreateUser(displayName);


            SharedPreferences.Editor editor = settings.edit();
            editor.putString("userID", userID);
            editor.putString("displayName", displayName);
            editor.apply();

            Intent i = new Intent(getApplicationContext(), v_home.class);
            startActivity(i);
        }
    };
}