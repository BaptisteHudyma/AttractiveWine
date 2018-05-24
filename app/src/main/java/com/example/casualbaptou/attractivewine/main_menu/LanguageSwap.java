package com.example.casualbaptou.attractivewine.main_menu;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.casualbaptou.attractivewine.R;

import java.util.Locale;

public class LanguageSwap {

    private static void changeLanguage(Resources res, String locale) {
        Configuration config;
        config = new Configuration(res.getConfiguration());

        switch (locale) {
            case "fr":
                config.setLocale(Locale.FRANCE);
                break;
            case "en":
                config.setLocale(Locale.ENGLISH);
                break;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
        
    }

    public static void createPopUp(){

        CharSequence language[] = new CharSequence[] {"Francais", "Anglais"};


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mainContext);
        builder.setTitle(MainActivity.mainContext.getString(R.string.selectLanguage));
        builder.setItems(language, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case(0):
                        Log.d("Alexis ","voici la partie 2 !");

                        LanguageSwap.changeLanguage(MainActivity.mainContext.getResources(),"fr");
                        break;
                    case(1):
                        LanguageSwap.changeLanguage(MainActivity.mainContext.getResources(),"en");

                }
            }
        });
        builder.show();
    }
}
