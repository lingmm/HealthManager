package com.shyling.healthmanager.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by shy on 2015/11/8.
 */
public class Utils {

    public static void Toast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }

    public static void Toast(Context context,int textid){
        Toast.makeText(context,textid,Toast.LENGTH_LONG).show();
    }

}
