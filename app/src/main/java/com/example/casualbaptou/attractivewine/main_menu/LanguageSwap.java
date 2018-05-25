package com.example.casualbaptou.attractivewine.main_menu;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.casualbaptou.attractivewine.R;

import java.util.Locale;

public class LanguageSwap {

    private void changeLanguage(Resources res, String locale) {
        Log.e("change anguage to", " french");

        Configuration config = res.getConfiguration();
        Log.e("    ", locale);
        switch (locale) {
            case "fr":
                //setLocale(Locale.FRANCE);
                config.setLocale(Locale.FRANCE);
                break;
            case "en":
                config.setLocale(Locale.ENGLISH);
                break;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public void createPopUp(){

        String []language = {"Francais", "Anglais"};


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mainContext);
        builder.setTitle(MainActivity.mainContext.getString(R.string.selectLanguage));

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("eeeeeeee", "  "+which);
                switch(which){
                        case 0:
                            changeLanguage(MainActivity.mainContext.getResources(),"fr");
                            break;
                        case 1:
                            changeLanguage(MainActivity.mainContext.getResources(),"en");
                            break;

                            default:
                                changeLanguage(MainActivity.mainContext.getResources(),"en");
                                break;
                }
            }
        };

        builder.setItems(language, dialogClickListener);
        builder.show();

    }
}
