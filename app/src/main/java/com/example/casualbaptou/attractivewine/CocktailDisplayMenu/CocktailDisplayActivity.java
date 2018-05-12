package com.example.casualbaptou.attractivewine.CocktailDisplayMenu;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casualbaptou.attractivewine.R;
import com.example.casualbaptou.attractivewine.RecipeDisplay.RecipeDisplayer;
import com.example.casualbaptou.attractivewine.URLRefs;

import java.util.List;

public class CocktailDisplayActivity extends AppCompatActivity implements cocktailAdapter.CocktailAdapterListener {
    public Context mainContext = this;
    private RecyclerView cocktailListView;
    private cocktailAdapter cocktailViewAdaptor;
    private RecyclerView.LayoutManager cocktailLayoutManager;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_display);
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
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
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
        return true;
    }

    public static TextView numberOfSelected;
    private  void setRecyclerView(){
        cocktailListView = (RecyclerView) findViewById(R.id.cocktailListDisp);
        //cocktailListView.setHasFixedSize(true);
        cocktailLayoutManager = new LinearLayoutManager(getApplicationContext());
        cocktailListView.setLayoutManager(cocktailLayoutManager);

        URLRefs urlRefs = new URLRefs();
        List<DisplayerContainer> cocktailNames = urlRefs.getAllCocktailNames(mainContext);
        numberOfSelected = findViewById(R.id.cocktailsLength);
        numberOfSelected.setText( cocktailNames.size() + " cocktails found");

        cocktailViewAdaptor = new cocktailAdapter(cocktailNames, this);
        cocktailListView.setAdapter(cocktailViewAdaptor);
        cocktailViewAdaptor.notifyDataSetChanged();
    }

}
