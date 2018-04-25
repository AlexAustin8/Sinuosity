


package com.example.alex.tuneup;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.app.Activity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.WindowManager;
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
                String lobbyID = entry.getText().toString();
                SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
                String userID = settings.getString("userID", "");

                RequestManager rManger = new RequestManager();
                rManger.web_lobbyGetData(lobbyID);
                if(rManger.loc_lobbyCheckExists()){
                    rManger.web_lobbyJoin(lobbyID, userID);
                    Intent i = new Intent(getApplicationContext(), v_lobby.class);
                    i.putExtra("lobbyKey", lobbyID);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(), "Lobby Not Found, Please Try Another Search", Toast.LENGTH_LONG).show();
                }



            }
        });




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