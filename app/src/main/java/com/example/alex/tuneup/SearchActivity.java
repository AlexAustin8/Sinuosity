package com.example.alex.tuneup;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ArrayList<JSONObject> results;
    TrackAdapter adapter;
    RadioGroup selections;
    RadioButton scSearch, spSearch;
    EditText searchBar;
    ListView resultList;
    String term;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // new Async_Search().execute("https://jailbreakme.ml/sinc/search/spotify.php", )

        //TODO: Create code to parse JSON into Track objects that are placed into results

        selections = this.findViewById(R.id.search_selection);
        spSearch = this.findViewById(R.id.spotify_search);
        scSearch = this.findViewById(R.id.soundcloud_search);
        searchBar = this.findViewById(R.id.search_input);
        resultList = this.findViewById(R.id.result_list);
        spSearch.toggle();
        searchBar.setOnKeyListener(mKeyListener);
        resultList.setOnItemClickListener(mListener);

        //Send a request to the server, and then parse the results into an
        //array of Track objects to put in the list. Then we can fill the list
        //Using the adapter


    }

    private View.OnKeyListener mKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            switch (selections.getCheckedRadioButtonId()) {
                case R.id.spotify_search:
                    term = searchBar.getText().toString();
                    Log.i("SearchTerm", term);
                    try {
                        results = new Async_Search().execute("https://jailbreakme.ml/sinc/search/spotify.php", term).get();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    adapter = new TrackAdapter(getApplicationContext(), results);
                    resultList.setAdapter(adapter);


                    break;
                case R.id.soundcloud_search:
                    term = searchBar.getText().toString();
                    try {
                        results = new Async_Search().execute("https://jailbreakme.ml/sinc/search/soundcloud.php", term).get();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    adapter = new TrackAdapter(getApplicationContext(), results);
                    resultList.setAdapter(adapter);


                    break;
            }
            return false;
        }

    };

    private AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            Toast.makeText(getApplicationContext(), "Song Added to Queue", Toast.LENGTH_SHORT);
            startActivity(i);

        }
    };



    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
    }



    //TODO: Create Async task that populates results and includes the following code in the onCompleted:
    //final TrackAdapter adapter = new TrackAdapter(getApplicationContext(), results)
    //resultList.setAdapter(adapter)



}
