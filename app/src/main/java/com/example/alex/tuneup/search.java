package com.example.alex.tuneup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class search extends Activity {
    ArrayList < JSONObject > results;
    TrackAdapter adapter;
    RadioGroup selections;
    RadioButton scSearch, spSearch;
    public EditText searchBar;
    ListView resultList;
    String term;

    private Timer timer = new Timer();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hides status and title bar -------------------------------------- requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.search);
        LayoutInflater inflater = getLayoutInflater();
        getWindow().addContentView(inflater.inflate(R.layout.search_top, null), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        // ----------------------------------------------------------------- selections = this.findViewById(R.id.search_selection);


        spSearch = this.findViewById(R.id.spotify_search);
        scSearch = this.findViewById(R.id.soundcloud_search);
        searchBar = this.findViewById(R.id.search_input);
        resultList = this.findViewById(R.id.result_list);
        spSearch.toggle();



        ImageView backButton = (ImageView) findViewById(R.id.bBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                 Intent i = new Intent(getApplicationContext(), lobby.class);
                 startActivity(i);
            }
        });


        EditText xxx = (EditText) search.this.findViewById(R.id.search_input);
        xxx.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            runSearch();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });



// Changes the search type variable
// ################################################################################################
        RadioGroup search_selection = (RadioGroup) findViewById(R.id.search_selection);
        search_selection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup search_selection, int checkedId) {
                EditText input = (EditText) findViewById(R.id.search_input);
                String curr = input.getText().toString();
                if(!curr.equals("")) {
                    runSearch();
                }
            }
        });
// ################################################################################################



        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                checkChange();
            }
        }, 0, 1000);


    }

    public String lastInput = "";
    public boolean checkChange() {

        EditText input = (EditText) findViewById(R.id.search_input);
        String curr = input.getText().toString();
        boolean change = false;

        if (!curr.equals(lastInput)) {
            change = true;
            Log.i("[me]", "[CHANGED] Last: " + lastInput + "  |  Curr: " + curr);
            runSearch();
        } else {
            change = false;
//            Log.i("[me]", "[UNCHANGED] Last: " + lastInput + "  |  Curr: " + curr);
        }

        lastInput = curr;
        return change;
    }



    public void runSearch() {

        String type = "spotify";
        RadioButton sc = (RadioButton) findViewById(R.id.soundcloud_search);


        if (sc.isChecked()) {
            type = "soundcloud";
        } else {
            type = "spotify";
        }


        EditText xx = search.this.findViewById(R.id.search_input);
        term = xx.getText().toString();
        Log.i("[me]", "[" +type+"] Searching " + term);



        try {
            results = new Async_Search().execute("https://jailbreakme.ml/sinc/search/" + type + ".php", term).get();
        } catch (Exception e) {
            Log.i("[me]", e.getMessage());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter = new TrackAdapter(getApplicationContext(), results);
                resultList.setAdapter(adapter);

            }
        });

    }



}