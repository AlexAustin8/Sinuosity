package com.example.alex.tuneup;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andrewclark on 4/21/18.
 */

public class RequestManager {




    // --------------------------------------------------------------------------------------
    private String serverAddress = "https://jailbreakme.ml/sinc/";

    private String displayName;

    private String lobbyID;
    private String lobbyName;
    private JSONArray lobbyMembers;
    private JSONArray lobbyPlaying;
    private JSONArray lobbyUpNext;
    private Boolean lobbyExists = true;

    private ArrayList<JSONObject> queue  = new ArrayList<>();
    private ArrayList<JSONObject> searchResults = new ArrayList<>();
    // --------------------------------------------------------------------------------------






    // Special methods
    // =============================================================================================
    // =============================================================================================
    // v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v


    private String pullSongValue(String index, JSONArray j) {
        try {
            switch (index) {
                case "uri":
                    return j.get(0).toString();

                case "name":
                    return j.get(1).toString();
                case "artist":
                    return j.get(2).toString();

                case "duration":
                    return j.get(3).toString();

                case "artwork":
                    return j.get(4).toString();

                case "source":
                    return j.get(5).toString();

                default:
                    return "Input Error";

            }

        } catch(Exception e) {
            return "Error";
        }
    }

    // ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^
    // =============================================================================================
    // =============================================================================================











    // Lobby
    // =============================================================================================
    // =============================================================================================
    // v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v

    public void net_lobbyCreate(String lobbyName, String userID) {
        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "lobby/create.php", lobbyName, userID).get();
        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }
    }

    // --------------------------------------------------------------------------------------

    public void net_lobbyJoin(String lobbyID, String userID) {
        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "lobby/join.php", lobbyID, userID).get();
        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }
    }

    // --------------------------------------------------------------------------------------

    public void net_lobbyLeave(String lobbyID, String userID) {

        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "lobby/leave.php", lobbyID, userID).get();
        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }

    }

    // --------------------------------------------------------------------------------------

    public String net_isInLobby(String lobbyID, String userID) {
        String returnData;
        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "lobby/inLobby.php", lobbyID, userID).get();
            returnData = response;
        } catch(Exception e) {
            Log.i("err", e.getMessage());
            returnData = "error";
        }
        return returnData;
    }

    // --------------------------------------------------------------------------------------

    public void net_lobbyGetData(String lobbyID) {

        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "lobby/getData.php", lobbyID).get();
            if(response.equals("Error")) {
                lobbyExists = false;
            } else {
                lobbyExists = true;
                JSONArray jsonArray;
                jsonArray = new JSONArray(response);
                JSONObject responseObj = jsonArray.getJSONObject(0);

                lobbyName = responseObj.getString("name");
                lobbyMembers = responseObj.getJSONArray("members");
                lobbyUpNext = responseObj.getJSONArray("upNext");
                lobbyPlaying = responseObj.getJSONArray("playing");
            }


        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }


    }

    // --------------------------------------------------------------------------------------

    public String loc_lobbyPlaying(String index) {
        return pullSongValue(index, lobbyPlaying);
    }

    public String loc_lobbyUpNext(String index) {
        return pullSongValue(index, lobbyUpNext);
    }

    public String loc_lobbyName() {
        return lobbyName;
    }

    public JSONArray loc_lobbyMembers() {
        return lobbyMembers;
    }

    public String loc_lobbyMembersCount() {
        return Integer.toString(lobbyMembers.length());
    }

    public Boolean loc_lobbyCheckExists() { return lobbyExists; }


    // ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^
    // =============================================================================================
    // =============================================================================================






    // Search
    // =============================================================================================
    // =============================================================================================
    // v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v

    public void net_searchGetData(String type, String term) {
        searchResults.clear();
        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "search/" + type + ".php", term).get();

            JSONArray jsonArray;


                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length() ; i++) {

                        JSONObject track = jsonArray.getJSONObject(i);
                        searchResults.add(track);
//                        String artist = object1.getString("artist");
                    }

                } catch(Exception e){
                    Log.i("Json Error", e.getMessage());
                }

    }

    // --------------------------------------------------------------------------------------


    public TrackAdapter loc_searchAdapter(Context context) {
        TrackAdapter adapter;
        adapter = new TrackAdapter(context, searchResults);
        return adapter;
    }


    // ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^
    // =============================================================================================
    // =============================================================================================









    // Queue
    // =============================================================================================
    // =============================================================================================
    // v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v

    public void net_queueGetData(String lobbyID) {

        queue.clear();
        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "/queue/getData.php", lobbyID).get();

            JSONArray jsonArray;


            jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length() ; i++) {

                JSONObject track = jsonArray.getJSONObject(i);
                queue.add(track);

            }

        } catch(Exception e){
            Log.i("Json Error", e.getMessage());
        }

    }

    // --------------------------------------------------------------------------------------

    public TrackAdapter loc_queueAdapter(Context context) {
        TrackAdapter adapter;
        adapter = new TrackAdapter(context, queue);
        return adapter;
    }

    // --------------------------------------------------------------------------------------

    public void net_queueAddSong(String userID, String lobbyID, String URI, String name, String artist, String duration, String artwork, String source) {

        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "/queue/addSong.php", userID, lobbyID, URI, name, artist, duration, artwork, source).get();
        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }

    }

    public void net_voteSong(Boolean upvote, String lobbyID, String songID) {

        String boolString = String.valueOf(upvote);

        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "/queue/voteSong.php", boolString, lobbyID, songID).get();
        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }

    }

    public ArrayList<JSONObject> loc_Queue() {
        return queue;
    }

    // ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^
    // =============================================================================================
    // =============================================================================================





    // Settings
    // =============================================================================================
    // =============================================================================================
    // v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v


    public void net_settingsChangeName(String userID, String newName) {
        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "user/changeName.php", userID, newName).get();
        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }
    }


    // ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^
    // =============================================================================================
    // =============================================================================================






}

