


package com.example.alex.tuneup;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.app.Activity;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.WindowManager;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

public class v_home extends Activity {
    public Button create, submit;
    public EditText entry;
    private String url = "https://thisisjustaplaceholderuntilwegetproperurl.gov";  //Replace with proper URL to connect with the server
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hides status and title bar --------------------------------------
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // -----------------------------------------------------------------

        setContentView(R.layout.home);


        LayoutInflater inflater = getLayoutInflater();
        getWindow().addContentView(inflater.inflate(R.layout.home_top, null),
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.FILL_PARENT));


        create = findViewById(R.id.create_button);
        submit = findViewById(R.id.submit_lobby_search);
        entry = findViewById(R.id.lobby_search);


        checkUserID();


        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
        String lobbyID = settings.getString("lobbyID", "");


        if(!lobbyID.equals("")) {
            joinLobby(lobbyID);
        }

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), v_create_lobby.class);
                startActivity(i);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
                String lobbyID = entry.getText().toString();

                SharedPreferences.Editor editor = settings.edit();
                editor.putString("lobbyID", lobbyID);
                editor.apply();

                joinLobby(lobbyID);

            }
        });



        // When user presses return key on keyboard, search function runs
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        entry.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:

                            SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
                            String lobbyID = entry.getText().toString();

                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("lobbyID", lobbyID);
                            editor.apply();

                            joinLobby(lobbyID);

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    }
    // Hides keyboard after pressing the enter key
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    protected void hideKeyboard() {

        InputMethodManager inputMethodManager = (InputMethodManager) v_home.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = v_home.this.getCurrentFocus();
        if (view == null) {
            if (inputMethodManager.isAcceptingText())
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        } else {
            if (view instanceof EditText)
                ((EditText) view).setText(((EditText) view).getText().toString()); // reset edit text bug on some keyboards bug
            inputMethodManager.hideSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    private void joinLobby(String lobbyID) {

        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
        String userID = settings.getString("userID", "");


        RequestManager rManger = new RequestManager();

        if(rManger.web_lobbyCheckExists(lobbyID)){

            rManger.web_lobbyJoin(lobbyID, userID);


            Intent i = new Intent(getApplicationContext(), v_lobby.class);
            i.putExtra("lobbyKey", lobbyID);
            startActivity(i);
        }else{
            Toast.makeText(getApplicationContext(), "Lobby not found.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkUserID() {

        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
//        settings.edit().remove("userID").commit();
//        settings.edit().remove("displayName").commit();
        String userID = settings.getString("userID", "");
        String displayName = settings.getString("displayName", "");
        if(userID.equals("")) {
            Intent i = new Intent(getApplicationContext(), v_first_open.class);
            startActivity(i);
        }

//        Toast.makeText(v_home.this, userID + " " + displayName, Toast.LENGTH_SHORT).show();

        return false;
    }

}