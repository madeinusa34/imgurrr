package com.example.epictureapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu1 extends AppCompatActivity {

    private Button button_gallery;
    private Button button_upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);
        button_gallery = (Button) findViewById(R.id.button5);
        button_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        button_upload = (Button) findViewById(R.id.button6);
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUpload();
            }
        });
    }
    public void openUpload(){
        Intent intent = new Intent(this,MainActivity2.class);
        startActivity(intent);
    }

    public void openGallery(){
        Intent intent = new Intent(this,gallery.class);
        startActivity(intent);
    }

}
