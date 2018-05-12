package com.example.casualbaptou.attractivewine;

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

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    public static String COCKTAILS_UPDATE = "com.example.casualbaptou.attractivewine.update.cocktails";
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
        MainCocktailLoaderIntent.startActionGetCocktail(mainContext);
        IntentFilter intentFilter = new IntentFilter(COCKTAILS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new onCocktailAPIUpdate(),intentFilter);
    }

    public class onCocktailAPIUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Button displayCocktailList = findViewById(R.id.CocktailList);
            displayCocktailList.setActivated(true);
            Log.i(TAG, "button set activeeeeeeeeeeeeeeeeeeeeeeee");

            URLRefs urlRefs = new URLRefs();
            urlRefs.displayCocktails("0cocktailArray.json", mainContext);
            urlRefs.displayCocktails("1cocktailArray.json", mainContext);
            urlRefs.displayCocktails("2cocktailArray.json", mainContext);
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
                                        }
        });

        pickRandomCocktail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"pick random pressed");
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
