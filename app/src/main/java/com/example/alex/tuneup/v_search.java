package com.example.alex.tuneup;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class v_search extends Activity {

    TrackAdapter adapter;
    RadioButton scSearch, spSearch;
    EditText searchBar;
    ListView resultList;
    String term;
    Context context;
    Timer timer = new Timer();
    int checkChangeCount = 0;
    String lastSearch = "";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hides title bar and overlays second layout
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LayoutInflater inflater = getLayoutInflater();
        getWindow().addContentView(inflater.inflate(R.layout.search_top, null),
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.FILL_PARENT));
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        spSearch = this.findViewById(R.id.spotify_search);
        scSearch = this.findViewById(R.id.soundcloud_search);
        searchBar = this.findViewById(R.id.search_input);
        resultList = this.findViewById(R.id.result_list);
        spSearch.toggle();
        context = getApplicationContext();




        // Enables back button on top nav bar
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        final LayoutInflater factory = getLayoutInflater();
        final View topLayout = factory.inflate(R.layout.lobby_top, null);
        ImageView backButton = (ImageView) topLayout.findViewById(R.id.bBack);



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Enter a song", Toast.LENGTH_SHORT).show();

            }
        });
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~




        // When user presses return key on keyboard, search function runs
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        EditText xxx = (EditText) v_search.this.findViewById(R.id.search_input);
        xxx.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
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
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~




        // Changes the variable that holds the search type value (Spotify or SoundCloud)
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        RadioGroup search_selection = (RadioGroup) findViewById(R.id.search_selection);
        search_selection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup search_selection, int checkedId) {
                EditText input = (EditText) findViewById(R.id.search_input);
                String curr = input.getText().toString();
                if (!curr.equals("")) {
                    runSearch();
                }
            }
        });

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~




        // A timer that runs on an interval to check if user has stopped typing their search term, preventing server from getting spammed with queries
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                EditText input = (EditText) findViewById(R.id.search_input);
                String currentTerm = input.getText().toString();


                if (checkChange()) {
                    checkChangeCount = 0;
                } else {
                    checkChangeCount = checkChangeCount + 1;

                }

                if (checkChangeCount >= 2) {
                    if (!lastSearch.equals(currentTerm)) {
                        runSearch();
                        checkChangeCount = 0;
                        lastSearch = currentTerm;
                    } else {
                        checkChangeCount = 0;
                    }

                }





            }
        }, 0, 600);
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    }



    // Checks to see if the search edittext has been updated
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public String lastInput = "";
    public boolean checkChange() {

        EditText input = (EditText) findViewById(R.id.search_input);
        String curr = input.getText().toString();
        boolean change = false;

        if (!curr.equals(lastInput)) {
            change = true;

        } else {
            change = false;
        }

        lastInput = curr;
        return change;
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~




    // Hides keyboard after pressing the enter key
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    protected void hideKeyboard() {

        InputMethodManager inputMethodManager = (InputMethodManager) v_search.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = v_search.this.getCurrentFocus();
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





    // Performs the search function
    // Creates an async task that reaches out to the server with a search query, and returns the adapter.
    // By returning an adapter verses the json string, it prevents the UI from hanging
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void runSearch() {

        String type = "spotify";
        RadioButton sc = (RadioButton) findViewById(R.id.soundcloud_search);


        if (sc.isChecked()) {
            type = "soundcloud";
        } else {
            type = "spotify";
        }


        EditText searchText = v_search.this.findViewById(R.id.search_input);
        term = searchText.getText().toString();

        if(!term.equals("")) {



        try {
            Async_Search asynccc = new Async_Search(context);

            adapter = asynccc.execute("https://jailbreakme.ml/sinc/search/" + type + ".php", term).get();
        } catch (Exception e) {
            Log.i("[+] Search Error", e.getMessage());
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultList.setAdapter(adapter);

            }
        });
        } else {
            Toast.makeText(getApplicationContext(), "Enter a song", Toast.LENGTH_SHORT).show();

        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



}