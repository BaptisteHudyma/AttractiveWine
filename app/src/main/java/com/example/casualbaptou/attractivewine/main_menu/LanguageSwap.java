package com.example.casualbaptou.attractivewine.main_menu;

import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageSwap {

    public static void changeLanguage(Resources res, String locale) {
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
        //LanguageHelper.changeLocale(this.getRessources(), “fr”);
    }
}
