package com.example.casualbaptou.attractivewine;

import android.content.Context;
import android.util.Log;
import com.example.casualbaptou.attractivewine.cocktail_display_menu.DisplayerContainer;
import com.example.casualbaptou.attractivewine.main_menu.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.Comparator;
import java.util.List;


public class URLRefs{
    private String TAG = "URL refs :";
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

    public static List<DisplayerContainer> allCocktails = new ArrayList<>();

    public List<DisplayerContainer> getAllCocktailNames(){
        List<DisplayerContainer> allNames = new ArrayList<>();

        for(int i = 0; i <  Categories.length; i++)
        {
            String fileName = FileNames[i] + ".json";
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
        try{
            JSONObject obj = loadJson(fileName, MainActivity.mainContext);
            if(obj == null)
                return new ArrayList<>();

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
            Log.e(TAG, "Unable to create json local file");
        }
        return thoseNames;
    }

    private JSONObject loadJson(String filename, Context context){
        if(!filename.endsWith(".json"))
            filename +=".json";

        try{
            InputStream is = context.openFileInput( filename );
            if(is == null)
            {
                Log.e(TAG, "Input stream for " + filename + " is empty");
                return null;
            }
            InputStreamReader input = new InputStreamReader(is);
            BufferedReader bf = new BufferedReader( input );
            StringBuilder fileCOntent = new StringBuilder();
            String currentLine;

            while( (currentLine = bf.readLine()) != null  ){
                fileCOntent.append(currentLine);
            }

            bf.close();
            input.close();
            return new JSONObject(  fileCOntent.toString() );

        }
        catch(FileNotFoundException e){
            Log.e(TAG, "File " + filename + " not found");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveCocktailAtURL(String stringUrl, String fileName) {
        try
        {
            URL url = new URL( stringUrl );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream IN;

            if (HttpURLConnection.HTTP_OK == conn.getResponseCode() && (IN = conn.getInputStream()) != null ) {
                return saveJson(IN, fileName, MainActivity.mainContext );
            }
            else
                Log.e(TAG, "URL " + url + " has given no answers");

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveJson(InputStream in, String file, Context context ){
        if(!file.endsWith(".json"))
            file += ".json";

        try{
            BufferedReader bf = new BufferedReader( new InputStreamReader(in));
            OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));

            StringBuilder finalStrg = new StringBuilder();
            String line;
            while( (line = bf.readLine()) != null )
            {
                finalStrg.append(line);
            }

            out.write(finalStrg.toString());

            in.close();
            out.close();
            return true;
        }
        catch( IOException e){
            Log.e(TAG, "Unable to write in file " + file);
        }
        return false;
    }

    public static void saveCocktailJson(String content, String filename, Context context){
        if(!filename.endsWith(".json") )
            filename += ".json";

        File path = context.getFilesDir();
        File cocktailFile = new File(path, filename);
        try{
            FileOutputStream stream = new FileOutputStream(cocktailFile);
            stream.write(content.getBytes());
            stream.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public static String loadStringJson(String filename, Context context){
        if(!filename.endsWith(".json"))
            filename +=".json";

        File path = context.getFilesDir();
        try{
            File file = new File(path, filename);
            int length = (int) file.length();

            byte [] bytes = new byte[length];
            FileInputStream in = new FileInputStream(file);
            in.read(bytes);
            in.close();
            return new String(bytes);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean cocktailIsSaved( String cocktailName, Context context ){
        if(!cocktailName.endsWith(".json")){
            cocktailName += ".json";
        }

        try {
            InputStream is = context.openFileInput(cocktailName);
            //TODO do more tests to see if file is really readable

            return true;
        }
        catch(FileNotFoundException e){
            return false;
        }
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
