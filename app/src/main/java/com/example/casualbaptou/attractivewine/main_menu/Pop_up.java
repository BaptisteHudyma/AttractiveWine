package com.example.casualbaptou.attractivewine.main_menu;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.casualbaptou.attractivewine.R;

public class Pop_up {
    boolean test;
    public boolean createPopUp(){



        String []YesNo = {"Yes", "No"};



        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mainContext);
        builder.setTitle(MainActivity.mainContext.getString(R.string.alert_massiv_dowload));


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch(which){

                    case 0:
                        Log.i("swich","j'ai été selectionné !!");
                        test = true;
                        break;
                    case 1:
                        Log.i("swich","Stéphanie de monaco !");
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
