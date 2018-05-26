package com.example.casualbaptou.attractivewine.recipe_display;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casualbaptou.attractivewine.NetworkConnection;
import com.example.casualbaptou.attractivewine.R;
import com.example.casualbaptou.attractivewine.URLRefs;
import com.example.casualbaptou.attractivewine.main_menu.MainActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


public class RecipeDisplayer extends AppCompatActivity {
    private String TAG = "Recipe displayer : ";

    public static String COCKTAILS_RECIPE_FINISHED= "com.example.casualbaptou.attractivewine.update.cocktailRecipe";
    public static String recipeFile;
    public static String cocktailID;
    private String message;

    private ShareActionProvider share_action;
    private Intent shareIntent = new Intent(Intent.ACTION_SEND);
    private cocktailRecipeTemplate cocktailRecipe;
    private Menu optionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_displayer);
        cocktailID = getIntent().getStringExtra("EXTRA_cocktail_ID");

        RelativeLayout RL = findViewById(R.id.loading_panel);
        RL.setAlpha(1);
        RL.setVisibility(View.VISIBLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Button reroolRandom = findViewById(R.id.rerollButton);
        if(cocktailID.length()<1) {
            reroolRandom.setVisibility(View.VISIBLE);
            if(!NetworkConnection.getInstance(this).isAvailable()){
                int indice = ThreadLocalRandom.current().nextInt(0, URLRefs.allCocktails.size());
                cocktailID = URLRefs.allCocktails.get(indice).getID();
            }
        }
        else
            reroolRandom.setVisibility(View.INVISIBLE);

        //TODO: keep track of the actual random cocktail when turning the phone
        launchRecipePulling();

        reroolRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cocktailID = "";
                if(!NetworkConnection.getInstance(getApplicationContext()).isAvailable()){
                    int indice = ThreadLocalRandom.current().nextInt(0, URLRefs.allCocktails.size());
                    cocktailID = URLRefs.allCocktails.get(indice).getID();
                }
                Log.i(TAG,"pick random pressed");
                findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                RelativeLayout RL = findViewById(R.id.loading_panel);
                RL.setAlpha(1);
                RL.setVisibility(View.VISIBLE);


                launchRecipePulling();
            }
        });
    }

    private void launchRecipePulling(){
        if(NetworkConnection.getInstance(this).isAvailable() || URLRefs.cocktailIsSaved( String.valueOf(cocktailID), getApplicationContext())  ) {
            startCocktailAPIreading();
        }
        else
        {
            findViewById(R.id.no_connection).setAlpha(1);
            //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            RelativeLayout RL = findViewById(R.id.loading_panel);
            RL.setAlpha(0);
            RL.setVisibility(View.GONE);
        }
    }

    private void updateMessage(int id){
        message =  getResources().getString(R.string.share_text) + "\nhttps://www.thecocktaildb.com/drink.php?c=" + id;
        shareIntent.putExtra( Intent.EXTRA_TEXT, message);
        share_action.setShareIntent(shareIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_button, menu);
        MenuItem share = menu.findItem(R.id.action_share);

        optionMenu = menu;

        share_action = (ShareActionProvider) MenuItemCompat.getActionProvider(share);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        message += cocktailID;
        shareIntent.putExtra( Intent.EXTRA_TEXT, message );

        share_action.setShareIntent(shareIntent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.favorite_star:
                if(item.isChecked()){   //remove from favorite
                    item.setIcon(R.drawable.grey_star);
                    item.setChecked(false);
                    if(isFavorite(cocktailRecipe.getName()))
                        removeFavorite(cocktailRecipe.getName());
                }
                else
                {
                    item.setIcon(R.drawable.yellow_star);
                    item.setChecked(true);
                    saveFavorite(cocktailRecipe.getName());
                }
                break;
        }
        return true;
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
            TextView quantities = findViewById(R.id.quantities);
            TextView category = findViewById(R.id.category);

            ImageView cocktailthumb = findViewById(R.id.cocktailImage);
            TextView mainRecipe = findViewById(R.id.recipe);
            TextView lastModified = findViewById(R.id.lastModified);

            cocktailRecipe = setRecipeView(recipeFile);
            URLRefs.saveCocktailJson(recipeFile, String.valueOf( cocktailRecipe.getId() ), MainActivity.mainContext);
            if(cocktailRecipe == null)
                return;

            cocktailTitle.setText( cocktailRecipe.getName() );
            mainRecipe.setText( buildCoherentRecipeFormat(cocktailRecipe.getMainRecipe()) );
            category.setText(cocktailRecipe.getCategory());

            RelativeLayout RL = findViewById(R.id.loading_panel);
            RL.setAlpha(0);
            RL.setVisibility(View.GONE);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            findViewById(R.id.no_connection).setAlpha(0);

            Picasso.get().load(cocktailRecipe.getImageLink()).into(cocktailthumb);
            lastModified.setText( cocktailRecipe.getLastTimeModified() );

            quantities.setText(getFormatIngredients( cocktailRecipe.getIngredients() ));



            updateMessage( cocktailRecipe.getId() );

            MenuItem fav = optionMenu.findItem(R.id.favorite_star);
            if(isFavorite(cocktailRecipe.getName()))    {
                fav.setIcon(R.drawable.yellow_star);
                fav.setChecked(true);
            }
            else    {
                fav.setIcon(R.drawable.grey_star);
                fav.setChecked(false);
            }
        }
    }

    private String buildCoherentRecipeFormat(String recipe){
        StringBuilder mainRec = new StringBuilder( recipe );
        int index = 1;
        while((index = mainRec.indexOf(".", index)) > 0 && index < mainRec.length()-2)
        {
            if(mainRec.charAt(index+1) == '\n')
            {
                index++;
                continue;
            }
            else if(mainRec.charAt(index+1) == '.')
            {
                index+=2;
                continue;
            }

            if(Character.isDigit(mainRec.charAt(index-1)) )
            {
                mainRec.insert(index-1, "\n");
                index++;
            }
            else if( mainRec.charAt(index+1) == ' '  )
                mainRec.replace(index+1, index+2, "\n");
            index++;
        }
        return mainRec.toString();
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
                thisCocktail.setId( Integer.parseInt(JsStrinf.getString("idDrink")) );
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        return thisCocktail;
    }

    private String getFormatIngredients(List<doubleString> ingredientCouples)
    {
        StringBuilder quan = new StringBuilder();

        for(doubleString entry : ingredientCouples) {
            //B is quantity
            //A is ingredient
            if(entry.B.endsWith("\n"))
            {
                quan.append("- ").append(entry.A).append(" ").append(entry.B);
            }
            else
            {
                if(entry.B.endsWith(" "))
                    quan.append("- ").append(entry.B).append(entry.A).append("\n");
                else
                    quan.append("- ").append(entry.B).append(" ").append(entry.A).append("\n");
            }


        }
        return quan.toString();
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
                if( ing.length() < 1 || ing.compareTo("null")==0)
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

    public void saveFavorite(String name) {
        //save cocktailRecipe
        SharedPreferences prefs = getSharedPreferences("Favorite_cocktails", MODE_PRIVATE);
        Set<String> favoriteSet = prefs.getStringSet("Favorite_cocktails", null );
        if(favoriteSet == null)
            favoriteSet = new HashSet<>();
        favoriteSet.add(name);

        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putStringSet("Favorite_cocktails", favoriteSet);
        editor.apply();
        editor.commit();

        Toast.makeText(this, name + " added to favorite", Toast.LENGTH_LONG).show();
    }

    public void removeFavorite(String name) {
        //save cocktailRecipe
        SharedPreferences sharedPref = getSharedPreferences("Favorite_cocktails", MODE_PRIVATE);
        Set<String> favoriteSet = sharedPref.getStringSet("Favorite_cocktails", null );
        if(favoriteSet == null)
            return;
        favoriteSet.remove(name);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putStringSet("Favorite_cocktails", favoriteSet);
        editor.apply();
        editor.commit();
    }

    private boolean isFavorite(String name) {
        //save cocktailRecipe
        SharedPreferences sharedPref = getSharedPreferences("Favorite_cocktails", MODE_PRIVATE);
        Set<String> favoriteSet = sharedPref.getStringSet("Favorite_cocktails", null );

        return favoriteSet!=null && favoriteSet.contains(name);
    }
}
