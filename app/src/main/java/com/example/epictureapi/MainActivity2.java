package com.example.epictureapi;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.session.MediaSession;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import lombok.experimental.var;

import static android.system.OsConstants.EACCES;

public class MainActivity2 extends AppCompatActivity {

    public static final int REQUEST_CODE_PICK_IMAGE = 1001;

    private static final String AUTHORIZATION_URL = "https://api.imgur.com/oauth2/authorize";
    private static final String CLIENT_ID = "480cdf2c81306bc";

    private LinearLayout rootView;
    private Button button = null;

    String accessToken = AuthenticationDialog.accessToken;
    // private String refreshToken;

    private String picturePath = "";
    private Button send;
    private Button ttr;

    private String uploadedImageUrl = "";
    private WebView webView;
    private ListView listView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //setContentView(R.layout.islog);
        //listView = (ListView) findViewById(R.id.pagination_list2);
        rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(llp);
        rootView.addView(tv);
        button = findViewById(R.id.chooseImage);
        setContentView(rootView);

        String action = getIntent().getAction();

        Button chooseImage = new Button(this);
        rootView.addView(chooseImage);
        chooseImage.setText("Choose image");
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                System.out.println(accessToken);
                System.out.println(picturePath);
            }
        });

        send = new Button(this);
        rootView.addView(send);
        send.setText("send to imgur");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(accessToken);
                System.out.println(picturePath);
                if (picturePath != null && picturePath.length() > 0 &&
                        accessToken != null && accessToken.length() > 0) {
                    (new UploadToImgurTask()).execute(picturePath);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (send == null) return;
        if (picturePath == null || picturePath.length() == 0) {
            send.setVisibility(View.GONE);
        } else {
            send.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("tag", "request code : " + requestCode + ", result code : " + resultCode);
        if (data == null) {
            Log.d("tag", "data is null");
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Log.d("tag", "image path : " + picturePath);
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Here is the upload task
    class UploadToImgurTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            //System.out.println("ok");
            final String upload_to = "https://api.imgur.com/3/upload";
            //System.out.println(upload_to);

            HttpClient httpClient = new DefaultHttpClient();
            System.out.println(httpClient);
            HttpContext localContext = new BasicHttpContext();
            System.out.println(localContext);
            HttpPost httpPost = new HttpPost(upload_to);
            System.out.println(httpClient);

            try {
                HttpEntity entity = MultipartEntityBuilder.create()
                        .addPart("image", new FileBody(new File(params[0])))
                        .build();
                System.out.println(entity);


                httpPost.setHeader("Authorization", "Bearer " + accessToken);
                httpPost.setEntity(entity);
                final HttpResponse response = httpClient.execute(httpPost,
                        localContext);

                final String response_string = EntityUtils.toString(response
                        .getEntity());

                final JSONObject json = new JSONObject(response_string);

                Log.d("tag", json.toString());

                JSONObject data = json.optJSONObject("data");
                uploadedImageUrl = data.optString("link");
                Log.d("tag", "uploaded image url : " + uploadedImageUrl);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean.booleanValue()) {
                Button openBrowser = new Button(MainActivity2.this);
                rootView.addView(openBrowser);
                openBrowser.setText("Open Browser");
                openBrowser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setData(Uri.parse(uploadedImageUrl));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        }
    }

}

