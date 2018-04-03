package com.example.alex.tuneup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ArrayList<Track> results = new ArrayList<>();
    RadioGroup selections;
    RadioButton bySong, byArtist;
    EditText searchBar;
    ListView resultList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //TODO: Create code to parse JSON into Track objects that are placed into results

        selections = this.findViewById(R.id.search_selection);
        bySong = this.findViewById(R.id.song_search);
        byArtist = this.findViewById(R.id.artist_search);
        searchBar = this.findViewById(R.id.search_input);
        resultList = this.findViewById(R.id.result_list);
        bySong.toggle();
        searchBar.setOnKeyListener(mKeyListener);
        resultList.setOnItemClickListener(mListener);

        //Send a request to the server, and then parse the results into an
        //array of Track objects to put in the list. Then we can fill the list
        //Using the adapter


    }

    private View.OnKeyListener mKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            switch(selections.getCheckedRadioButtonId()){
                case R.id.song_search:
                    //TODO: Make server request to return search results by Song so that results is populated, call an Async task

                    break;
                case R.id.artist_search:
                    //TODO: Make server request to return search results by artists (Spotify only) so that results is populated, call an Async task
            }
            return false;
        }

    };

    private AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            //Make server request to post selected item.

            //Return to the lobby activity.
            startActivity(i);
        }
    };



    //TODO: Create Async task that populates results and includes the following code in the onCompleted:
    //final TrackAdapter adapter = new TrackAdapter(getApplicationContext(), results)
    //resultList.setAdapter(adapter)



}
