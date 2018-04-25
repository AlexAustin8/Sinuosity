package com.example.alex.tuneup;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.net.URL;
import java.util.List;
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
    pl.droidsonroids.gif.GifImageView loaderIcon;
    RequestManager rm;
    Handler handler;

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


        rm = new RequestManager();
        spSearch = this.findViewById(R.id.spotify_search);
        scSearch = this.findViewById(R.id.soundcloud_search);
        searchBar = this.findViewById(R.id.search_input);
        resultList = this.findViewById(R.id.result_list);
        spSearch.toggle();
        context = getApplicationContext();
        loaderIcon = findViewById(R.id.loaderIcon);


        loaderIcon.setVisibility(View.GONE);


        // Enables back button on top nav bar
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        ImageView backButton = (ImageView) findViewById(R.id.bBackSearch);



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_search.this.onBackPressed();
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
                            resultList.setVisibility(View.INVISIBLE);
                            loaderIcon.setVisibility(View.VISIBLE);
                            runSearch();
                            threadTwo();

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
                    resultList.setVisibility(View.INVISIBLE);
                    loaderIcon.setVisibility(View.VISIBLE);
                    runSearch();
                    threadTwo();


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
                        runOnUiThread(new Runnable() {
                            public void run() {
                                threadTwo();
                            }
                        });

                        checkChangeCount = 0;
                        lastSearch = currentTerm;
                    } else {
                        checkChangeCount = 0;
                    }

                }





            }
        }, 0, 600);
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        ListView lv = findViewById(R.id.result_list);
        lv.setOnItemClickListener(onItemClickListener);




    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        }

    };






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


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    public void runSearch() {


    }


    private void threadTwo() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                adapter = rm.loc_searchAdapter(getApplicationContext());
                resultList.setAdapter(adapter);

                resultList.setVisibility(View.VISIBLE);
                loaderIcon.setVisibility(View.INVISIBLE);
            }
        };

        Runnable runnable = new Runnable() {
            public void run() {
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();

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


                        rm.web_searchGetData(type,term);

                    } catch (Exception e) {
                        Log.i("[+] Search Error", e.getMessage());
                    }

                } else {
//            Toast.makeText(getApplicationContext(), "Enter a song", Toast.LENGTH_SHORT).show();

                }

                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        };

        Thread mythread = new Thread(runnable);
        mythread.start();
    }

}