package ru.ifmo.android_2015.citycam.webcams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by anton on 07.11.15.
 */
public class DataWebCam {
    private final URL url;
    private final String user;
    private Bitmap image;
    private boolean isDownloaded;
    private AsyncTask<URL, Void, Bitmap> at;


    DataWebCam(URL url, String user){
        this.url = url;
        this.user = user;
        isDownloaded = false;
        at = new AsyncTask<URL, Void, Bitmap>() {
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
        };
    }

    public URL getURL(){
        return url;
    }

    public String getUser() {
        return user;
    }

    public void setImage(Bitmap bitmap){
        isDownloaded = true;
        this.image = bitmap;
    }

    public Bitmap getImage(){
        if(isDownloaded)
            return image;
        else {
            try {
                return at.execute(url).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public boolean isDownloaded(){
        return isDownloaded;
    }
}
