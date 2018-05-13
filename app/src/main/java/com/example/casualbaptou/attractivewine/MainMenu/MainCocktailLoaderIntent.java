package com.example.casualbaptou.attractivewine.MainMenu;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.casualbaptou.attractivewine.URLRefs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class MainCocktailLoaderIntent extends IntentService {

    private static final String ACTION_get_cocktail_API = "com.example.casualbaptou.attractivewine.action.cocktails";


    public MainCocktailLoaderIntent() {
        super("MainCocktailLoaderIntent");
    }

    public static void startActionGetCocktail(Context context) {
        try{
            Intent intent = new Intent(context, MainCocktailLoaderIntent.class);
            intent.setAction(ACTION_get_cocktail_API);
            context.startService(intent);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_get_cocktail_API.equals(action)) {
                saveCocktailLists();
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.COCKTAILS_UPDATE));
            }
        }
    }

    private void saveCocktailLists() {
        try{
            for(int i=0; i<URLRefs.Categories.length; i++)
            {
                URL url = new URL( URLRefs.URLbase + URLRefs.Refs[7] + URLRefs.Categories[i] );
                Log.i(TAG, url.toString());
                saveCocktailAtURL(url, URLRefs.FileNames[i] + ".json");
            }
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
    }

    private void saveCocktailAtURL(URL url, String fileName) {
        try
        {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.connect();

            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                File f = new File(getCacheDir(), fileName);
                copyInputStreamToFile(conn.getInputStream(), f );
                Log.i(TAG,  fileName + " cocktail list json downloaded");
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    private void copyInputStreamToFile(InputStream in, File file){
        try {
            OutputStream out = new FileOutputStream(file, true);
            byte[] buf = new byte[in.available()];
            int lenght;
            while ((lenght = in.read(buf)) > 0) {
                out.write(buf, 0, lenght);
                Log.i(TAG, buf.toString());
            }
            out.close();
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
