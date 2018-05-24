package com.example.casualbaptou.attractivewine;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.casualbaptou.attractivewine.cocktail_display_menu.DisplayerContainer;
import com.example.casualbaptou.attractivewine.main_menu.MainActivity;

public class DownloadEveryCocktailsIntent extends IntentService {

    private URLRefs uRef = new URLRefs();
    public static String ACTION_download_all = "com.example.casualbaptou.attractivewine.action.dAll";

    public DownloadEveryCocktailsIntent(){
        super("DownloadEveryCocktailsIntent");
    }

    public static void startActionGetCocktail(Context context) {
        try{
            Intent intent = new Intent(context, DownloadEveryCocktailsIntent.class);
            intent.setAction(ACTION_download_all);
            context.startService(intent);

            //TODO : notification starts
        }
        catch(Exception e)
        {
            Log.e("Download every cocktails intent : ", "Intent launch failed");
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_download_all.equals(action)) {

                for(DisplayerContainer cocktail : URLRefs.allCocktails){
                    if( !uRef.saveCocktailAtURL( URLRefs.URLbase + URLRefs.Refs[0] + cocktail.getCocktailName(), cocktail.getID() ) ){
                        //TODO : alert of non download
                    }
                    //TODO : update notification state
                }

                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.DOWLOAD_FINISHED));
            }
        }
    }




}
