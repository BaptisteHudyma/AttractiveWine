package com.example.casualbaptou.attractivewine;

import android.content.Context;
import android.util.Log;

import com.example.casualbaptou.attractivewine.CocktailDisplayMenu.DisplayerContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.Comparator;
import java.util.List;
import static android.content.ContentValues.TAG;


public class URLRefs{
    public static String URLbase = "https://www.thecocktaildb.com/api/json/v1/1/";
    public static String []Refs = {   "search.php?s="   //0 //+cocktail name : return recipe
                                    , "search.php?i="   //1 //+ingredient : return ingredient description
                                    , "lookup.php?i="   //2 //+id : return a precise recipe
                                    , "filter.php?i="   //3 //+ingredient : return all cocktails containing those ingredients

                                    , "filter.php?a=Alcoholic"          //4 //return all cocktails in this category
                                    , "filter.php?a=Non_Alcoholic"      //5 //return all cocktails in this category
                                    , "filter.php?a=Optional_alcohol"   //6 //return all cocktails in this category

                                    , "filter.php?c="   //7 //+Category : return all the cocktails in this category
                                    , "list.php?c="     //8 //get all categories
                                    , "list.php?i="     //9 //get all ingredients
                                    , "list.php?g="     //10//get all glass types
                                    , "random.php"      //11//get a random cocktail
                                    };
    public static String []Categories = {
            "Ordinary_Drink",
            "Cocktail",
            "Milk%20/%20Float%20/%20Shake",
            "Other/Unknown",
            "Cocoa",
            "Shot",
            "Coffee%20/%20Tea",
            "Homemade_Liqueur",
            "Punch%20/%20Party_Drink",
            "Beer",
            "Soft_Drink%20/%20Soda"
    };

    public static String []FileNames = {
            "ordinarydrink",
            "cocktail",
            "milkFloatShake",
            "otherUnknown",
            "cocoa",
            "shot",
            "coffeeTea",
            "homemadeLiqueur",
            "punchPartyDrink",
            "beer",
            "softDrinkSoda"
    };

    private List<DisplayerContainer> allCocktails = new ArrayList<>();

    public List<DisplayerContainer> getAllCocktailNames(Context thisContext){
        List<DisplayerContainer> allNames = new ArrayList<>();
        for(int i = 0; i <  Categories.length; i++)
        {
            String fileName = thisContext.getCacheDir() + "/" + FileNames[i] + ".json";
            Log.i(TAG, FileNames[i] + ".json");
            allNames.addAll(displayCocktails(fileName));
        }

        allNames = sortDispContainerList(allNames);

        if(allNames.size() != allCocktails.size())
            allCocktails = allNames;
        return allNames;
    }

    private List<DisplayerContainer> displayCocktails(String fileName){

        List<DisplayerContainer> thoseNames = new ArrayList<>();
        String jsonFile = loadJSON( fileName );
        try{
            JSONObject obj = new JSONObject(   jsonFile  );
            JSONArray m_jArry = obj.getJSONArray("drinks");
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject JsStrinf = m_jArry.getJSONObject(i);

                DisplayerContainer DP = new DisplayerContainer();
                DP.setCocktailName(JsStrinf.getString("strDrink"));
                DP.setID(JsStrinf.getString("idDrink"));
                DP.setImageLink(JsStrinf.getString("strDrinkThumb"));
                thoseNames.add(DP);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return thoseNames;
    }

    private String loadJSON(String fileName) {
        String json;
        try {
            InputStream is = new FileInputStream( fileName );
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
}

    private List<DisplayerContainer> sortDispContainerList(List<DisplayerContainer> destination){
        destination.sort(new Comparator<DisplayerContainer>() {
            @Override
            public int compare(DisplayerContainer cocktail1, DisplayerContainer cocktail2)
            {
                return  cocktail1.getCocktailName().compareTo(cocktail2.getCocktailName());
            }
        });
        return destination;
    }

}
