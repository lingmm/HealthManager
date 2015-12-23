package com.shyling.healthmanager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.shyling.healthmanager.HealthManagerApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shy on 2015/11/8.
 */
public class Utils {
    private static Toast toast;
    static SharedPreferences data;

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
    //保存账号密码
    public static boolean saveUser(Context context,String userName,String passWd){
        data = context.getSharedPreferences("info",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = data.edit();
        edit.putString("_userNumber",userName);
        edit.putString("_passWd", passWd);
        edit.commit();
        return true;
    }
    //读取账号密码
    public static Map<String,String> getUser(Context context){
        data = context.getSharedPreferences("info",Context.MODE_PRIVATE);
        String userName = data.getString("_userNumber",null);
        String passWd = data.getString("_passWd",null);
        Map<String,String> userMap = new HashMap<String,String>();
        userMap.put("_userNumber",userName);
        userMap.put("_passWd",passWd);
        return userMap;
    }

}
