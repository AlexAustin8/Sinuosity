package com.example.alex.tuneup;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by andrewclark on 4/21/18.
 */

public class RequestManager {




    // --------------------------------------------------------------------------------------
    private String serverAddress = "https://jailbreakme.ml/sinc/";

    private String displayName;

    private String lobbyID;
    private String lobbyAdmin;
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

    public String web_lobbyCreate(String lobbyName, String userID) {
        String response = "";
        try {
            SendRequest async = new SendRequest();
            response = async.execute(serverAddress + "lobby/create.php", "lobbyName", lobbyName, "userID", userID).get();


        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }
        return response;
    }

    // --------------------------------------------------------------------------------------

    public void web_lobbyJoin(String lobbyID, String userID) {
        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "lobby/join.php", "lobbyID", lobbyID, "userID", userID).get();

        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }
    }

    // --------------------------------------------------------------------------------------

    public void web_lobbyLeave(String lobbyID, String userID) {

        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "lobby/leave.php", "lobbyID", lobbyID, "userID", userID).get();
        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }

    }

    // --------------------------------------------------------------------------------------

    public String web_isInLobby(String userID) {
        String response = "";
        try {
            SendRequest async = new SendRequest();
            response = async.execute(serverAddress + "lobby/inLobby.php", "userID", userID).get();

        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }
        return response;
    }

    // --------------------------------------------------------------------------------------

    public void web_lobbyGetData(String lobbyID) {

        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "lobby/getData.php", "lobbyID", lobbyID).get();
            if(response.equals("Error")) {
                lobbyExists = false;
            } else {
                lobbyExists = true;
                JSONArray jsonArray;
                jsonArray = new JSONArray(response);
                JSONObject responseObj = jsonArray.getJSONObject(0);

                lobbyName = responseObj.getString("name");
                lobbyAdmin = responseObj.getString("admin");
                lobbyMembers = responseObj.getJSONArray("members");
                lobbyUpNext = responseObj.getJSONArray("upNext");
                lobbyPlaying = responseObj.getJSONArray("playing");
            }


        } catch(Exception e) {
            Log.i("Get Data Error", e.getMessage());

        }


    }

    // --------------------------------------------------------------------------------------

    public Boolean web_lobbyCheckExists(String lobbyID) {
        Boolean exists = false;
        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "lobby/checkExists.php", "lobbyID", lobbyID).get();
            Log.i("lobbyFound", response);

            if(response.equals("true")) {
                exists = true;
            }

        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }
        return exists;
    }

    // --------------------------------------------------------------------------------------

    public String loc_lobbyPlaying(String index) {
        return URLDecoder.decode(URLDecoder.decode(pullSongValue(index, lobbyPlaying)));
    }

    public String loc_lobbyUpNext(String index) {
        return URLDecoder.decode(URLDecoder.decode(pullSongValue(index, lobbyUpNext)));
    }

    public String loc_lobbyName() {
        return URLDecoder.decode(URLDecoder.decode(lobbyName));
    }

    public String loc_lobbyAdmin() {
        return lobbyAdmin;
    }

    public JSONArray loc_lobbyMembers() {
        return lobbyMembers;
    }

    public String loc_lobbyMembersCount() {
        return Integer.toString(lobbyMembers.length());
    }




    // ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^
    // =============================================================================================
    // =============================================================================================












    // Search
    // =============================================================================================
    // =============================================================================================
    // v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v  v

    public void web_searchGetData(String type, String term) {
        searchResults.clear();
        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "search/" + type + ".php", "query", term).get();
            Log.e("Search results:" , response);
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

    public void web_queueGetData(String lobbyID) {

        queue.clear();
        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "/queue/getData.php","lobbyID", lobbyID).get();

            JSONArray jsonArray= new JSONArray(response);

            for (int i = 0; i < jsonArray.length() ; i++) {

                JSONObject track = jsonArray.getJSONObject(i);
                queue.add(track);

            }

        } catch(Exception e){
            Log.i("Json Error", e.getMessage());
        }

    }

    // --------------------------------------------------------------------------------------

    public QueueAdapter loc_queueAdapter(Context context) {
        QueueAdapter adapter;
        adapter = new QueueAdapter(context, queue);
        return adapter;
    }

    // --------------------------------------------------------------------------------------

    public void web_queueAddSong(String userID, String lobbyID, String URI, String name, String artist, String duration, String artwork, String source) {

        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "/queue/addSong.php", "userID", userID, "lobbyID", lobbyID, "URI", URI, "name", name, "artist", artist, "duration", duration, "artwork", artwork, "src", source).get();
        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }

    }

    // --------------------------------------------------------------------------------------

    public void web_queueShift(String lobbyID) {

        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "/queue/shift.php", "lobbyID", lobbyID).get();
        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }

    }

    // --------------------------------------------------------------------------------------

    public void web_queueVoteSong(String upvote, String lobbyID, String songID) {

        String boolString = String.valueOf(upvote);

        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "/queue/voteSong.php", "upvote", boolString,"lobbyID", lobbyID,"songID", songID).get();
        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }

    }

    // --------------------------------------------------------------------------------------



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


    public void web_settingsChangeName(String userID, String newName) {
        try {
            SendRequest async = new SendRequest();
            String response = async.execute(serverAddress + "settings/changeName.php", "userID", userID, "newName", newName).get();
        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }
    }

    public String web_settingsCreateUser(String displayName) {
        String response = "";
        try {
            SendRequest async = new SendRequest();
            response = async.execute(serverAddress + "settings/createUser.php", "displayName", displayName).get();

        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }
        return response;
    }


    // ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^  ^
    // =============================================================================================
    // =============================================================================================






}

