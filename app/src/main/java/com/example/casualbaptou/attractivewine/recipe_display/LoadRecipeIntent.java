package com.example.casualbaptou.attractivewine.recipe_display;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.casualbaptou.attractivewine.URLRefs;
import com.example.casualbaptou.attractivewine.main_menu.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
        Log.i("Load recipe intent", URLRefs.URLbase + URLRefs.Refs[2] + ID);
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_get_cocktail_API.equals(action)) {

                if( ID.length() < 1 || !URLRefs.cocktailIsSaved( ID, MainActivity.mainContext) ) {
                    String url;

                    if (ID.length() < 1)   //Random pick
                        url = URLRefs.URLbase + URLRefs.Refs[11];
                    else                //chosen pick
                        url = URLRefs.URLbase + URLRefs.Refs[2] + ID;

                    InputStream is = getinputStream(url);
                    if (is == null)
                        return;

                    RecipeDisplayer.recipeFile = convertStreamToString(is);
                }
                else{
                    RecipeDisplayer.recipeFile = URLRefs.loadStringJson(ID, MainActivity.mainContext);
                }

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
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
