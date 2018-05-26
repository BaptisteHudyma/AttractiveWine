package com.example.casualbaptou.attractivewine.main_menu;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.casualbaptou.attractivewine.DownloadEveryCocktailsIntent;
import com.example.casualbaptou.attractivewine.R;

public class Pop_up {
    private boolean test=true;


    public boolean createPopUp(){



        String []YesNo = {"Yes", "No"};



        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mainContext);
        builder.setTitle(MainActivity.mainContext.getString(R.string.alert_massiv_dowload));


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){

                    case 0:
                        Toast.makeText(MainActivity.mainContext ,R.string.start_dl,Toast.LENGTH_SHORT).show();
                        DownloadEveryCocktailsIntent.startActionGetCocktail(MainActivity.mainContext);
                        break;
                    case 1:
                        test = false;
                        break;

                    default:
                        test = false;
                        break;
                }
            }

        };
        builder.setItems(YesNo, dialogClickListener);
        builder.show();

        return test;
    }
}
