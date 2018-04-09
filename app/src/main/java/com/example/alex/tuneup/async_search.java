package com.example.alex.tuneup;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by andrewclark on 3/8/18.
 */

public class async_search extends AsyncTask<String, String, String> {
    protected void onPreExecute(){}

    protected String doInBackground(String... params) {

        try {

            URL url = new URL(params[0]);

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("query", params[1]);

            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            }
            else {
                return new String("false : "+responseCode);
            }
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        int resultLen = result.length();
//        result = result.substring(1, resultLen-1);
        Log.i("Post Exec", result);


        JSONArray jsonArray;
        try{
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length() ; i++) {
                try{
                    JSONObject object1 = jsonArray.getJSONObject(i);
                    String uri = object1.getString("uri");
                    String title = object1.getString("title");
                    String artist = object1.getString("artist");
                    String duration = object1.getString("duration");
                    String artwork_url = object1.getString("artwork_url");
                    Log.i("objPrint", "------------------------");
                    Log.i("objPrint", "URI: " + uri);
                    Log.i("objPrint", "title: " + title);
                    Log.i("objPrint", "artist: " + artist);
                    Log.i("objPrint", "duration: " + duration);
                    Log.i("objPrint", "artwork: " + artist);

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        } catch(Exception e) {
            Log.i("Json Error", e.getMessage());
        }




    }





    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}






