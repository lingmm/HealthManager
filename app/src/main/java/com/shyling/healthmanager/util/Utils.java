package com.shyling.healthmanager.util;

import android.util.Log;
import android.widget.Toast;

import com.shyling.healthmanager.HealthManagerApplication;

/**
 * Created by shy on 2015/11/8.
 */
public class Utils {
    private static Toast toast;

    /*
    Toast
    @param text
    单例Toast,新的Toast会替换旧的
     */
    public static void Toast(String text) {
        if (toast == null) {
            synchronized (Toast.class) {
                toast = Toast.makeText(HealthManagerApplication.healthManagerApplication, null, Toast.LENGTH_LONG);
            }
        }
        toast.setText(text);
        toast.show();
    }

    public static void Toast(int textid) {
        Utils.Toast(HealthManagerApplication.healthManagerApplication.getString(textid));
    }


    public static void Log(Object o) {
        if (o instanceof String) {
            Log.d(Const.TAG, (String) o);
        } else {
            Log.d(Const.TAG, o.toString());
        }
    }

}
