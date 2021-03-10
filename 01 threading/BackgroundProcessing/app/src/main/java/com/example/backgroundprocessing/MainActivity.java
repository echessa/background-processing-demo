package com.example.backgroundprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageView = findViewById(R.id.image);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = new java.net.URL("https://raw.githubusercontent.com/echessa/imgs/master/auth0/android_background_processing/android_jelly_bean.png").openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}