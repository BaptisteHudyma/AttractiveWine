package com.example.casualbaptou.attractivewine.recipe_display;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.casualbaptou.attractivewine.URLRefs;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class LoadRecipeIntent extends IntentService {

    private static final String ACTION_get_cocktail_API = "com.example.casualbaptou.attractivewine.action.cocktailLoad";
    private static String ID;

    public LoadRecipeIntent(){
        super("load this recipe");
    }


    public static void startRecipePulling(Context context){
        try{
            ID = RecipeDisplayer.cocktailID;
            Intent intent = new Intent(context, LoadRecipeIntent.class);
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
        Log.i(TAG, URLRefs.URLbase + URLRefs.Refs[2] + ID);
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_get_cocktail_API.equals(action)) {
                String url;

                if(ID.length()<1)   //Random pick
                    url = URLRefs.URLbase + URLRefs.Refs[11];
                else                //chosen pick
                    url = URLRefs.URLbase + URLRefs.Refs[2] + ID;

                RecipeDisplayer.recipeFile = convertStreamToString(getinputStream( url ));
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(RecipeDisplayer.COCKTAILS_RECIPE_FINISHED));
            }
        }
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    private InputStream getinputStream(String Url){
        try{
            URL url = new URL( Url );
            return saveCocktailAtURL(url);
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream saveCocktailAtURL(URL url) {
        try
        {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                return conn.getInputStream();
            } else
                Log.e(TAG, "Can't access API");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
