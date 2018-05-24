package com.example.casualbaptou.attractivewine;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.casualbaptou.attractivewine.cocktail_display_menu.DisplayerContainer;
import com.example.casualbaptou.attractivewine.main_menu.MainActivity;
import com.squareup.picasso.Picasso;

public class DownloadEveryCocktailsIntent extends IntentService {

    private URLRefs uRef = new URLRefs();
    public static String ACTION_download_all = "com.example.casualbaptou.attractivewine.action.dAll";

    public DownloadEveryCocktailsIntent(){
        super("DownloadEveryCocktailsIntent");
    }


    private static Notification.Builder notifBuilder;
    private static NotificationManager notifManager;
    private static Notification notif;

    private final Context currentContext = MainActivity.mainContext;

    public static void startActionGetCocktail(Context context) {
        try{
            Intent intent = new Intent(context, DownloadEveryCocktailsIntent.class);
            intent.setAction(ACTION_download_all);
            context.startService(intent);

            notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notifBuilder = new Notification.Builder(context);

            notifBuilder.setOngoing(true)
                        .setContentTitle(context.getString(R.string.recipe_download_notif_title) )
                        .setContentText("")
                        .setProgress(100, 0, false)
                        .setSmallIcon(R.mipmap.attractive_wine_icon);

            notif = notifBuilder.build();
            notifManager.notify(42, notif);
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
            if (ACTION_download_all.equals(action)) {
                float currentProgress = 0;

                for(DisplayerContainer cocktail : URLRefs.allCocktails){
                    if( NetworkConnection.getInstance(currentContext).isAvailable() && uRef.saveCocktailAtURL( URLRefs.URLbase + URLRefs.Refs[0] + cocktail.getCocktailName(), cocktail.getID() ) ){
                        notifBuilder.setProgress(100, (int)currentProgress, false);
                        currentProgress += (float)100/URLRefs.allCocktails.size();
                        notif = notifBuilder.build();
                        notifManager.notify(42, notif);
                        Picasso.get().load(cocktail.getImageLink());
                    }
                    else
                    {
                        Log.e("Downloader : ", " Error while downloading " + cocktail.getCocktailName());
                    }
                }

                notifBuilder.setContentText("")
                            .setContentTitle(currentContext.getString(R.string.recipe_download_notif_title))
                            .setOngoing(false)
                            .setProgress(100, 100, false);
                notif = notifBuilder.build();
                notifManager.notify(42, notif);

            }
        }
    }




}
