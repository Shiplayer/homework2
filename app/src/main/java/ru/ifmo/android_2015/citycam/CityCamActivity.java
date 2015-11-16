package ru.ifmo.android_2015.citycam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.ifmo.android_2015.citycam.model.City;
import ru.ifmo.android_2015.citycam.webcams.AsyncDownloadWebCamImage;
import ru.ifmo.android_2015.citycam.webcams.DataWebCam;
import ru.ifmo.android_2015.citycam.webcams.Webcams;

/**
 * Экран, показывающий веб-камеру одного выбранного города.
 * Выбранный город передается в extra параметрах.
 */
public class CityCamActivity extends AppCompatActivity {

    /**
     * Обязательный extra параметр - объект City, камеру которого надо показать.
     */
    public static final String EXTRA_CITY = "city";

    private City city;

    private static ImageView camImageView;
    private static ProgressBar progressView;
    private TextView name;
    private Button btn_prev;
    private Button btn_next;
    private static List<DataWebCam> listImage;
    private static int indexImage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        city = getIntent().getParcelableExtra(EXTRA_CITY);
        if (city == null) {
            Log.w(TAG, "City object not provided in extra parameter: " + EXTRA_CITY);
            finish();
        }

        setContentView(R.layout.activity_city_cam);
        camImageView = (ImageView) findViewById(R.id.cam_image);
        progressView = (ProgressBar) findViewById(R.id.progress);

        getSupportActionBar().setTitle(city.name);

        progressView.setVisibility(View.VISIBLE);
        AsyncTask<URL, Void, Bitmap> at = null;
        listImage = null;
        Bitmap image = null;
        try {
            at = new AsyncDownloadWebCamImage().execute(Webcams.createNearbyUrl(city.latitude, city.longitude));
            assert at != null;
            image = at.get();
        } catch (InterruptedException | MalformedURLException | ExecutionException e) {
            e.printStackTrace();
        }
        if(image != null) {
            camImageView.setImageBitmap(image);
            progressView.setVisibility(View.GONE);
            name = (TextView)findViewById(R.id.UserName);
            name.setVisibility(View.VISIBLE);
        } else {
            camImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_not_found));
            progressView.setVisibility(View.GONE);
        }
        // Здесь должен быть код, инициирующий асинхронную загрузку изображения с веб-камеры
        // в выбранном городе.
    }


    private static final String TAG = "CityCam";
}
