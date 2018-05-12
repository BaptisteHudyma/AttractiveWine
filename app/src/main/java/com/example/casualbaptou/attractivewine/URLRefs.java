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
import java.util.List;
import static android.content.ContentValues.TAG;

public class URLRefs{
    public static String URLbase = "https://www.thecocktaildb.com/api/json/v1/1/";
    public static String []Refs = {   "search.php?s="   //0 //+cocktail name : return recipe
                                    , "search.php?i="   //1 //+ingredient : return ingredient description
                                    , "lookup.php?i="   //2 //+id : return a precise recipe
                                    , "filter.php?i="   //3 //+ingredient : return all cocktails containing those ingredients

                                    , "filter.php?a=Alcoholic"          //4 //+category : return all cocktails in this category
                                    , "filter.php?a=Non_Alcoholic"      //5 //+category : return all cocktails in this category
                                    , "filter.php?a=Optional_alcohol"   //6 //+category : return all cocktails in this category

                                    , "filter.php?c="   //7 //+Category : return all the cocktails in this category
                                    , "list.php?c="     //8 //get all categories
                                    , "list.php?i="     //9 //get all ingredients
                                    , "list.php?g="     //10//get all glass types
                                    , "random.php"      //11//get a random cocktail
                                    };

    public List<DisplayerContainer> allCocktails = new ArrayList<>();

    public List<DisplayerContainer> getAllCocktailNames(Context thisContext){
        if(allCocktails.size() > 10)
            return allCocktails;

        List<DisplayerContainer> allNames = new ArrayList<>();
        allNames.addAll(displayCocktails("0cocktailArray.json", thisContext));
        allNames.addAll(displayCocktails("1cocktailArray.json", thisContext));
        allNames.addAll(displayCocktails("2cocktailArray.json", thisContext));

        //TODO : sort the names
        //String[] cocktailNames = new String[allNames.size()];
        //return allNames.toArray(cocktailNames);

        allCocktails = allNames;
        return allNames;
    }

    public List<DisplayerContainer> displayCocktails(String fileName, Context mC){
        String jsonFile = loadJSON( fileName, mC );
        List<DisplayerContainer> thoseNames = new ArrayList<>();
        try{
            JSONObject obj = new JSONObject(jsonFile);
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

    public String loadJSON(String fileName, Context mC) {
        String json = null;
        try {
            Log.i(TAG, mC.getCacheDir()+"/"+fileName);
            InputStream is = new FileInputStream(mC.getCacheDir()+"/"+fileName);
            json = getJSONfromInputStream(is);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String getJSONfromInputStream(InputStream is)
    {
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        }
        catch( IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /*private List<DisplayerContainer> sortDispContainerList(){

    }*/

}
