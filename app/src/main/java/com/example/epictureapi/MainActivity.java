package com.example.epictureapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements AuthenticationListener {

   private String token = null;
   private AppPreferences appPreferences = null;
   private AuthenticationDialog authenticationDialog = null;
   private Button button = null;
   private View info = null;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.activity_main);

   button = findViewById(R.id.btn_login);
   info = findViewById(R.id.info);
   appPreferences = new AppPreferences(this);
   token = appPreferences.getString(AppPreferences.TOKEN);
   }

   @Override
   public void onTokenReceived(String auth_token) {
       if (auth_token == null) {
           System.out.println("Epictureapi semble avoir trouvé une erreur.");
       }
   }

   public void onClick(View view) {
       System.out.println("test");
       if (token != null) {
           System.out.println("Oups! Le token est vide!");
       }
       else {
           authenticationDialog = new AuthenticationDialog(this, this);
           authenticationDialog.setCancelable(true);
           authenticationDialog.show();
       }
   }
}