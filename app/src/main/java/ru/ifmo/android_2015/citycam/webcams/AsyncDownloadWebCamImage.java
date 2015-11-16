package ru.ifmo.android_2015.citycam.webcams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anton on 07.11.15.
 */
public class AsyncDownloadWebCamImage extends AsyncTask<URL, Void, Bitmap> {
    static int count;
    @Override
    protected Bitmap doInBackground(URL... params) {
        count = 0;
        Bitmap image = null;
        try {
            Log.i("url", params[0].toString());
            URLConnection urlConnection = params[0].openConnection();
            urlConnection.connect();
            List<DataWebCam> list = new ArrayList<>();
            JsonReader reader = new JsonReader(new InputStreamReader(urlConnection.getInputStream()));
            list = readJsonMessage(new JsonReader(new InputStreamReader(urlConnection.getInputStream())));
            if(list.size() != 0){
                URLConnection imageURLConnection = list.get(0).getURL().openConnection();
                imageURLConnection.connect();
                image = BitmapFactory.decodeStream(imageURLConnection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private List<DataWebCam> readJsonMessage(JsonReader reader) throws IOException {
        List<DataWebCam> list = new ArrayList<>();
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            switch (name) {
                case "webcams":
                    Log.i("json webcams data", name);
                    list = readJsonMessage(reader);
                    break;
                case "webcam":
                    reader.beginArray();
                    while(reader.hasNext()){
                        list.add(readMessage(reader));
                    }
                    reader.endArray();
                    break;
                default:
                    Log.i("json skip", name);
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return list;
    }

    private DataWebCam readMessage(JsonReader reader) throws IOException {
        String url = null;
        String user = null;
        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals("user")){
                user = reader.nextString();
            }else if(name.equals("preview_url")){
                url = reader.nextString();
                Log.i("url" + count++, url);
            } else
                reader.skipValue();
        }
        reader.endObject();
        assert url != null;
        return new DataWebCam(new URL(url), user);
    }
}
