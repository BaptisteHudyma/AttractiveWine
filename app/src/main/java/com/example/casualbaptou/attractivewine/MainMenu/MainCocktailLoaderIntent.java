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
                saveCocktailList();
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.COCKTAILS_UPDATE));
            }
        }
    }

    private void saveCocktailList() {
        try{
            URL url = new URL( URLRefs.URLbase + URLRefs.Refs[8] );
            Log.i(TAG, URLRefs.URLbase + URLRefs.Refs[8]);
            InputStream IS = saveCocktailAtURL(url, 100);



            for(int i=0; i<3; i++)
            {
                url = new URL( URLRefs.URLbase + URLRefs.Refs[4+i] );
                Log.i(TAG, URLRefs.URLbase + URLRefs.Refs[4+i]);
                saveCocktailAtURL(url, i);

            }
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
    }

    private InputStream saveCocktailAtURL(URL url, int index) {
        try
        {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                File f = new File(getCacheDir(), index + "cocktailArray.json");
                InputStream IS = conn.getInputStream();
                copyInputStreamToFile(IS, f);
                Log.i(TAG, "Cocktail list " + (index + 1) + " json downloaded");
                return IS;
            } else
                Log.e(TAG, "Can't access API");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private void copyInputStreamToFile(InputStream in, File file)
    {   //puts the json in a file
        try {
            OutputStream out = new FileOutputStream(file, true);
            byte[] buf = new byte[1024];
            int lenght;
            while ((lenght = in.read(buf)) > 0) {
                out.write(buf, 0, lenght);
            }
            out.close();
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
