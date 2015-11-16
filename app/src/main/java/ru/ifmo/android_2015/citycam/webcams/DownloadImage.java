package ru.ifmo.android_2015.citycam.webcams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anton on 07.11.15.
 */
public class DownloadImage extends AsyncTask<URL, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(URL... params) {
        HttpURLConnection urlConnection = null;
        Bitmap image = null;
        try {
            urlConnection = (HttpURLConnection) params[0].openConnection();
            urlConnection.connect();
            image = BitmapFactory.decodeStream(urlConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
