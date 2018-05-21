package com.example.casualbaptou.attractivewine.cocktail_display_menu;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.example.casualbaptou.attractivewine.main_menu.MainActivity;
import com.example.casualbaptou.attractivewine.R;
import com.example.casualbaptou.attractivewine.recipe_display.RecipeDisplayer;
import com.example.casualbaptou.attractivewine.URLRefs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CocktailDisplayActivity extends AppCompatActivity implements cocktailAdapter.CocktailAdapterListener {
    private cocktailAdapter cocktailViewAdaptor;
    private boolean isFavoriteMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_display);
        isFavoriteMenu = Boolean.parseBoolean(getIntent().getStringExtra("EXTRA_isFavorite"));
        setRecyclerView();
    }

    @Override
    public void onCocktailSelected(DisplayerContainer cocktail){
        //when a cocktail is selected
        //Toast.makeText(getApplicationContext(), "Selected: " + cocktail.getCocktailName(), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, RecipeDisplayer.class);
        intent.putExtra("EXTRA_cocktail_ID", cocktail.getID());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        // Associate searchable configuration with the SearchView
        createSearchAction(menu);
        createFilterTab(menu);
        return true;
    }

    private List<String> loadFavoriteList() {
        //save cocktailRecipe
        SharedPreferences sharedPref = getSharedPreferences("Favorite_cocktails", MODE_PRIVATE);
        Set<String> favoriteSet = sharedPref.getStringSet("Favorite_cocktails", null);
        if (favoriteSet == null) {
            favoriteSet = new HashSet<>();
        }
        return new ArrayList<>( favoriteSet );
    }

    private void createFilterTab(Menu menu){
        //TODO: implement filter menu here
    }

    private void createSearchAction(Menu menu){
        SearchView searchView;
        try{
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) menu.findItem(R.id.action_search)
                    .getActionView();
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // filter recycler view when query submitted
                    cocktailViewAdaptor.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    // filter recycler view when text is changed
                    cocktailViewAdaptor.getFilter().filter(query);
                    return false;
                }
            });
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
        // listening to search query text change
    }

    public static TextView numberOfSelected;
    private  void setRecyclerView(){
        RecyclerView cocktailListView;
        RecyclerView.LayoutManager cocktailLayoutManager;

        cocktailListView = findViewById(R.id.cocktailListDisp);
        //cocktailListView.setHasFixedSize(true);
        cocktailLayoutManager = new LinearLayoutManager(getApplicationContext());
        cocktailListView.setLayoutManager(cocktailLayoutManager);

        URLRefs urlRefs = new URLRefs();
        List<DisplayerContainer> cocktailNames = urlRefs.getAllCocktailNames(MainActivity.mainContext);

        if(isFavoriteMenu)
        {
            List<String> favList = loadFavoriteList();
            List<DisplayerContainer> finalList = new ArrayList<>();
            for(DisplayerContainer cocktail : cocktailNames)
            {
                if( favList.contains( cocktail.getCocktailName()) )
                {
                    finalList.add(cocktail);
                    favList.remove(cocktail.getCocktailName());
                }
            }
            cocktailNames = finalList;
        }
        numberOfSelected = findViewById(R.id.cocktailsLength);

        String numberSelected = String.valueOf(cocktailNames.size()) + " cocktails found";
        numberOfSelected.setText( numberSelected );

        cocktailViewAdaptor = new cocktailAdapter(cocktailNames, this);
        cocktailListView.setAdapter(cocktailViewAdaptor);
        cocktailViewAdaptor.notifyDataSetChanged();
    }

}
