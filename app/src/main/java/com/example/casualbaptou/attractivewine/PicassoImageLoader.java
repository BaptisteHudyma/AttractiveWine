package com.example.casualbaptou.attractivewine;

import android.app.Application;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class PicassoImageLoader extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        Picasso.Builder build = new Picasso.Builder(this);
        build.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));

        Picasso built = build.build();
        //built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}
