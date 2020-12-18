package com.example.first_list_view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ListView list_view;
    String name, o_name;

    private static String JSON_LINK="https://api.github.com/";

    ArrayList<HashMap<String,String>> ownerlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ownerlist = new ArrayList<>();
        list_view = findViewById(R.id.lv);

        GetData getData = new GetData();
        getData.execute();
    }


    public class GetData extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            String Correct = "";

            try {

                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(JSON_LINK);
                    urlConnection = (HttpURLConnection) url.openConnection();


                    InputStream ip = urlConnection.getInputStream();
                    InputStreamReader sread = new InputStreamReader(ip);
                    int data = sread.read();
                    while (data != -1) {
                        Correct += (char) data;
                        data = sread.read();
                    }
                    return Correct;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return Correct;

        }

        @Override
        protected void onPostExecute(String s){

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("repositories");

                for(int i=0; i< jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    name = jsonObject1.getString( "name");
                    o_name = jsonObject1.getString( "login");
                    HashMap<String, String> friends = new HashMap<>();
                    friends.put("name",name);
                    friends.put("ownername",o_name);
                    ownerlist.add(friends);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this,
                    ownerlist,
                    R.layout.list_layout,
                    new String[] {"name", "ownername"},
                    new int[]{R.id.ntv, R.id.otv});

            list_view.setAdapter(adapter);


        }

        public void execute() {
        }
    }
}