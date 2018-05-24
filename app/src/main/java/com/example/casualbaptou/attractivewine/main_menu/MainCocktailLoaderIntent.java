package com.example.casualbaptou.attractivewine.main_menu;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.casualbaptou.attractivewine.NetworkConnection;
import com.example.casualbaptou.attractivewine.R;
import com.example.casualbaptou.attractivewine.URLRefs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainCocktailLoaderIntent extends IntentService {

    private static String TAG = "Cocktail lists loader : ";
    private static final String ACTION_get_cocktail_API = "com.example.casualbaptou.attractivewine.action.cocktails";
    public static boolean isLoaded = true;


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
            Log.e(TAG, "Intent launch failed");
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_get_cocktail_API.equals(action)) {

                isLoaded = saveCocktailLists();
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.COCKTAILS_UPDATE));
            }
        }
    }

    private boolean saveCocktailLists() {
        for(int i = 0; i< URLRefs.Categories.length; i++) {
            if(!NetworkConnection.getInstance(this).isAvailable()){
                return URLRefs.cocktailIsSaved(URLRefs.FileNames[0], this);
            }
            try {

                URL url = new URL(URLRefs.URLbase + URLRefs.Refs[7] + URLRefs.Categories[i]);
                Log.i(TAG, "Attempt to download " + url.toString());
                saveCocktailAtURL(url, URLRefs.FileNames[i] + ".json");

            } catch (MalformedURLException e) {
                Log.e(TAG, "Url " + URLRefs.URLbase + URLRefs.Refs[7] + URLRefs.Categories[i] +"is malformed");
                //e.printStackTrace();
                return false;
            }
        }
        return true;

    }

    private void saveCocktailAtURL(URL url, String fileName) {
        try
        {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.i(TAG,  fileName + " cocktail list json downloaded");

            conn.connect();

            InputStream IN;

            if (HttpURLConnection.HTTP_OK == conn.getResponseCode() && (IN = conn.getInputStream()) != null ) {
                //File f = new File(getCacheDir(), fileName);
                //copyInputStreamToFile(conn.getInputStream(), f );

                copyStreamToInput(IN, fileName, MainActivity.mainContext );
                //copyInputStreamToFile(IN, new File(getCacheDir(), fileName));

                Log.i(TAG,  fileName + " cocktail list json downloaded");
            }
            else
                Log.e(TAG, "URL " + url + " has given no answers");

        }
        catch (IOException e){
            Log.e(TAG, "URL " + url + " has given no answers");
        }
    }

    private void copyStreamToInput(InputStream in, String file, Context context ){
        try{
            BufferedReader bf = new BufferedReader( new InputStreamReader(in));
            OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));

            StringBuilder finalStrg = new StringBuilder();
            String line;
            while( (line = bf.readLine()) != null )
            {
                finalStrg.append(line);
            }

            out.write(finalStrg.toString());

            in.close();
            out.close();
        }
        catch( IOException e){
            Log.e(TAG, "Unable to write in file " + file);
        }
    }
}
