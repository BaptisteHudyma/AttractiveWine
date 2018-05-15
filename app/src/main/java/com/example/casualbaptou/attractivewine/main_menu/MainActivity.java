package com.example.casualbaptou.attractivewine.main_menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.casualbaptou.attractivewine.cocktail_display_menu.CocktailDisplayActivity;
import com.example.casualbaptou.attractivewine.R;
import com.example.casualbaptou.attractivewine.recipe_display.RecipeDisplayer;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    public static String COCKTAILS_UPDATE = "com.example.casualbaptou.attractivewine.update.cocktailUpdates";
    public static Context mainContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainContext = this;

        setContentView(R.layout.activity_main);
        setButtonActions();
        startCocktailAPIreading();
    }

    private void startCocktailAPIreading(){
        MainCocktailLoaderIntent.startActionGetCocktail(this);
        IntentFilter intentFilter = new IntentFilter(COCKTAILS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new onCocktailAPIUpdate(),intentFilter);
    }

    public class onCocktailAPIUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Button displayCocktailList = findViewById(R.id.CocktailList);
            displayCocktailList.setActivated(true);
        }
    }


    private void setButtonActions(){
        Button displayCocktailList = findViewById(R.id.CocktailList);
        displayCocktailList.setActivated(false);
        Button pickRandomCocktail = findViewById(R.id.Random);
        Button favoritedCocktails = findViewById(R.id.Favorite);

        displayCocktailList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"display cocktail list pressed");
                Intent cocktailListDisplay = new Intent(mainContext, CocktailDisplayActivity.class);
                startActivity(cocktailListDisplay);
            }
        });

        pickRandomCocktail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"pick random pressed");
                Intent intent = new Intent(mainContext, RecipeDisplayer.class);
                intent.putExtra("EXTRA_cocktail_ID", "");
                startActivity(intent);
            }
        });

        favoritedCocktails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"see favorited pressed");
            }
        });

    }
}
