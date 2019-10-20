package com.example.epictureapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class gallery extends AppCompatActivity {

    private OkHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        fetchData();
    }
    private static class Photo {
        String id;
        String title;
    }

    private void fetchData() {
        httpClient = new OkHttpClient.Builder().build();
        final Request request = new Request.Builder()
                .url("https://api.imgur.com/3/gallery/user/rising/0.json")
                .header("Authorization","Client-ID 480cdf2c81306bc")
                .header("User-Agent","Epicture")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(null, "An error has occurred " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // More code goes here
                System.out.println(response);
                JSONObject data = null;
                try {
                    data = new JSONObject(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray items = null;
                try {
                    items = data.getJSONArray("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final List<Photo> photos = new ArrayList<Photo>();

                for(int i=0; i<items.length();i++) {
                    JSONObject item = null;
                    try {
                        item = items.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Photo photo = new Photo();
                    try {
                        if(item.getBoolean("is_album")) {
                            photo.id = item.getString("cover");
                        } else {
                            photo.id = item.getString("id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        photo.title = item.getString("title");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    photos.add(photo);
                    System.out.println(photos.toString());// Add photo to list
                    //render(photos);
                }
            }
        });
    }
    private static class PhotoVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;

        public PhotoVH(View itemView) {
            super(itemView);
        }
    }
    private void render(final List<Photo> photos) {
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv_of_photos);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

}
