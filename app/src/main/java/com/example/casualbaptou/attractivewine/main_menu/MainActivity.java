package com.example.casualbaptou.attractivewine.main_menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.casualbaptou.attractivewine.DownloadEveryCocktailsIntent;
import com.example.casualbaptou.attractivewine.NetworkConnection;
import com.example.casualbaptou.attractivewine.R;
import com.example.casualbaptou.attractivewine.URLRefs;
import com.example.casualbaptou.attractivewine.cocktail_display_menu.CocktailDisplayActivity;
import com.example.casualbaptou.attractivewine.recipe_display.RecipeDisplayer;

public class MainActivity extends AppCompatActivity {
    public static String COCKTAILS_UPDATE = "com.example.casualbaptou.attractivewine.update.cocktailUpdates";
    public static Context mainContext;

    private String TAG = "Main activity :";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = this;


        findViewById(R.id.mainLoading).setAlpha(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        new URLRefs().getAllCocktailNames();    //update the total cocktail list

        setButtonActions();

        if (NetworkConnection.getInstance(this).isAvailable())
            startCocktailAPIreading();
        else
        {
            if(!URLRefs.cocktailIsSaved(URLRefs.FileNames[0], this))
            {
                findViewById(R.id.no_connection).setAlpha(1);

            }
            else
            {
                RelativeLayout RL = findViewById(R.id.mainLoading);
                RL.setVisibility(View.GONE);
                RL.findViewById(R.id.mainLoading).setAlpha(0);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){



        switch (item.getItemId()){
            case R.id.language:
                   new LanguageSwap().createPopUp();

                return true;
            case R.id.savePref:
                if(!NetworkConnection.getInstance(this).isWifi()) {
                    Pop_up pop = new Pop_up();
                    if(pop.createPopUp()) {
                        DownloadEveryCocktailsIntent.startActionGetCocktail(this);
                        return true;
                    }
                    else {
                        return true;
                    }
                }

                DownloadEveryCocktailsIntent.startActionGetCocktail(this);


                return true;
            case R.id.rinit:
                PreferenceManager.getDefaultSharedPreferences(mainContext).
                        edit().clear().apply();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void startCocktailAPIreading() {
        MainCocktailLoaderIntent.startActionGetCocktail(this);
        IntentFilter intentFilter = new IntentFilter(COCKTAILS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new onCocktailAPIUpdate(), intentFilter);
    }

    public class onCocktailAPIUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Button displayCocktailList = findViewById(R.id.CocktailList);
            displayCocktailList.setActivated(true);

            if(!MainCocktailLoaderIntent.isLoaded){
                findViewById(R.id.no_connection).setAlpha(1);
                return;
            }

            RelativeLayout RL = findViewById(R.id.mainLoading);
            RL.setVisibility(View.GONE);
            RL.findViewById(R.id.mainLoading).setAlpha(0);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    private void setButtonActions() {
        Button displayCocktailList = findViewById(R.id.CocktailList);
        displayCocktailList.setActivated(false);
        Button pickRandomCocktail = findViewById(R.id.Random);
        Button favoritedCocktails = findViewById(R.id.Favorite);


        displayCocktailList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "display cocktail list pressed");
                Intent cocktailListDisplay = new Intent(mainContext, CocktailDisplayActivity.class);
                cocktailListDisplay.putExtra("EXTRA_isFavorite", "false");
                startActivity(cocktailListDisplay);
            }
        });

        pickRandomCocktail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "pick random pressed");
                Intent intent = new Intent(mainContext, RecipeDisplayer.class);
                intent.putExtra("EXTRA_cocktail_ID", "");
                startActivity(intent);
            }
        });

        favoritedCocktails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "see favorited pressed");
                Intent cocktailListDisplay = new Intent(mainContext, CocktailDisplayActivity.class);
                cocktailListDisplay.putExtra("EXTRA_isFavorite", "true");
                startActivity(cocktailListDisplay);
            }
        });


    }

}
