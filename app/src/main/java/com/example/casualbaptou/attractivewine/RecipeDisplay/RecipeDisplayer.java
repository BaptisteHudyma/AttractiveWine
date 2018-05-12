package com.example.casualbaptou.attractivewine.RecipeDisplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.casualbaptou.attractivewine.MainMenu.MainCocktailLoaderIntent;
import com.example.casualbaptou.attractivewine.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class RecipeDisplayer extends AppCompatActivity {
    public static String COCKTAILS_RECIPE_FINISHED= "com.example.casualbaptou.attractivewine.update.cocktailRecipe";
    public static String recipeFile;
    public static String cocktailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_displayer);
        cocktailID = getIntent().getStringExtra("EXTRA_cocktail_ID");
        startCocktailAPIreading();

        Button reroolRandom = findViewById(R.id.rerollButton);
        if(cocktailID.length()<1)
            reroolRandom.setVisibility(View.VISIBLE);
        else
            reroolRandom.setVisibility(View.INVISIBLE);


        reroolRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"pick random pressed");
                startCocktailAPIreading();
            }
        });
    }



    private void startCocktailAPIreading(){
        LoadRecipeIntent.startRecipePulling(this);
        IntentFilter intentFilter = new IntentFilter(COCKTAILS_RECIPE_FINISHED);
        LocalBroadcastManager.getInstance(this).registerReceiver(new downloadFinished(), intentFilter);
    }

    public class downloadFinished extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

            TextView cocktailTitle = findViewById(R.id.main_title);
            TextView ingredients = findViewById(R.id.ingredients);
            TextView quantities = findViewById(R.id.quantities);

            ImageView cocktailthumb = findViewById(R.id.cocktailImage);
            TextView mainRecipe = findViewById(R.id.recipe);

            cocktailRecipeTemplate cocktailRecipe = setRecipeView(recipeFile);
            if(cocktailRecipe == null)
                return;

            cocktailTitle.setText( cocktailRecipe.getName() );
            mainRecipe.setText(cocktailRecipe.getMainRecipe() );
            Picasso.get().load(cocktailRecipe.getImageLink()).into(cocktailthumb);

            doubleString formatIngredients = getFormatIngredients( cocktailRecipe.getIngredients() );

            ingredients.setText(formatIngredients.A);
            quantities.setText(formatIngredients.B);
        }
    }

    private cocktailRecipeTemplate setRecipeView(String IS){
        if(IS == null)
            return null;
        cocktailRecipeTemplate thisCocktail = new cocktailRecipeTemplate();

        try{
            JSONObject jsonObj = new JSONObject(IS);
            JSONArray m_jArry = jsonObj.getJSONArray("drinks");
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject JsStrinf = m_jArry.getJSONObject(i);
                thisCocktail.setName( JsStrinf.getString("strDrink"));
                thisCocktail.setAlcoholic( JsStrinf.getString( "strAlcoholic" ) );
                thisCocktail.setGlassType( JsStrinf.getString( "strGlass" ) );
                thisCocktail.setCategory( JsStrinf.getString( "strCategory" ) );
                thisCocktail.setMainRecipe( JsStrinf.getString("strInstructions") );
                thisCocktail.setImageLink( JsStrinf.getString( "strDrinkThumb" ) );
                thisCocktail.setLastTimeModified( JsStrinf.getString( "dateModified" ) );
                thisCocktail.setIngredients( getIngredients(JsStrinf)  );
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        return thisCocktail;
    }

    private doubleString getFormatIngredients(List<doubleString> ingredientCouples)
    {
        doubleString finalIngredientRep = new doubleString("", "");

        for(doubleString entry : ingredientCouples) {
            String ingredient = entry.A;
            String measure = entry.B;

            finalIngredientRep.A += "\n" + ingredient;
            finalIngredientRep.B += "\n" + measure;
            //f(measure.length()>=1 && !measure.contains("of")  )
            //    finalIngredientRep.B += " of";
        }
        return finalIngredientRep;
    }

    private List<doubleString> getIngredients(JSONObject jsObj)
    {
        List<doubleString> ingredientCouples = new ArrayList<>();
        String ingredientRef = "strIngredient";
        String measure = "strMeasure";

        for(int i=1; i<=15; i++)
        {
            try{
                String ing = jsObj.getString( ingredientRef + i );
                if( ing.length() < 1)
                    break;

                doubleString element = new doubleString( ing, jsObj.getString(measure + i) );
                ingredientCouples.add(element);
            }
            catch(JSONException e){
                e.printStackTrace();
            }

        }
        return ingredientCouples;
    }
}
