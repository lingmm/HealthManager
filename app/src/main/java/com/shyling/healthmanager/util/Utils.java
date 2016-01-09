package com.shyling.healthmanager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.shyling.healthmanager.HealthManagerApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        userMap.put("_userNumber", userName);
        userMap.put("_passWd", passWd);
        return userMap;
    }
    public static String readInputStream(InputStream is){
        byte[] b = new byte[1024];
        int len = 0;
        //创建字节数组输出流，读取输入流的文本数据时，同步把数据写入数组输出流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while((len = is.read(b)) != -1){
                bos.write(b, 0, len);
            }
            //把字节数组输出流里的数据转换成字节数组
            String text = new String(bos.toByteArray());
            return text;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
