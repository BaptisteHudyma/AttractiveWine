package com.example.casualbaptou.attractivewine.CocktailDisplayMenu;

import android.content.Context;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.casualbaptou.attractivewine.R;
import com.example.casualbaptou.attractivewine.URLRefs;

import java.util.List;

import static android.content.ContentValues.TAG;

public class CocktailDisplayActivity extends AppCompatActivity {
    public Context mainContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_display);
        setRecyclerView();
    }

    private RecyclerView cocktailListView;
    private RecyclerView.Adapter cocktailViewAdaptor;
    private RecyclerView.LayoutManager cocktailLayoutManager;

    private  void setRecyclerView(){
        cocktailListView = (RecyclerView) findViewById(R.id.cocktailListDisp);
        //cocktailListView.setHasFixedSize(true);
        cocktailLayoutManager = new LinearLayoutManager(getApplicationContext());
        cocktailListView.setLayoutManager(cocktailLayoutManager);

        URLRefs urlRefs = new URLRefs();
        List<DisplayerContainer> cocktailNames = urlRefs.getAllCocktailNames(mainContext);

        cocktailViewAdaptor = new cocktailAdapter(cocktailNames);
        cocktailListView.setAdapter(cocktailViewAdaptor);
        cocktailViewAdaptor.notifyDataSetChanged();
    }
}
