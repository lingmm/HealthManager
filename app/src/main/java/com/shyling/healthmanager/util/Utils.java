package com.shyling.healthmanager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.shyling.healthmanager.HealthManagerApplication;
import com.shyling.healthmanager.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    private static Toast toast;
    static SharedPreferences data;
    public static String[] doctor = new String[]{"17703810458","17703810468","17703810466","17703810477","17703810478","17703810499","18266037256","15736982989"};
    public static String[] doctorName = new String[]{"赵医生", "钱医生", "孙医生", "李医生", "周医生", "吴医生","郑医生","王医生"};
    public static int[] dotorImage = new int[]{R.drawable.ic_1,R.drawable.ic_3,R.drawable.ic_4,R.drawable.ic_image,
            R.drawable.ic_2,R.drawable.ic_5,R.drawable.ic_6, R.drawable.ic_7};
    /*
    Toast
    @param text
    单例Toast,新的Toast会替换旧的
     */
    public static void Toast(String... text) {
        if (toast == null) {
            synchronized (Toast.class) {
                toast = Toast.makeText(HealthManagerApplication.healthManagerApplication, null, Toast.LENGTH_LONG);
            }
        }
        toast.setText(TextUtils.join("\n", text));
        toast.show();
    }

    public static void Toast(int textid) {
        Utils.Toast(HealthManagerApplication.healthManagerApplication.getString(textid));
    }
    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void Log(Object ...obj) {
        for(Object o : obj){
            if (o instanceof String) {
                Log.e(Const.TAG, (String) o);
            } else {
                Log.e(Const.TAG, o.toString());
            }
        }
    }
    //保存账号密码
    public static boolean saveUser(Context context,String userName,String passWd){
        data = context.getSharedPreferences("Info",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = data.edit();
        edit.putString("_userNumber",userName);
        edit.putString("_passWd", passWd);
        edit.apply();
        return true;
    }
    //读取账号密码
    public static String[] getUser(Context context){
        data = context.getSharedPreferences("Info",Context.MODE_PRIVATE);
        String userName = data.getString("_userNumber","");
        String passWd = data.getString("_passWd","");
        return new String[]{userName,passWd};
    }

    @Nullable
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
            e.printStackTrace();
        }
        return null;
    }

    public static boolean getNetworkAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        if(networkInfos!=null) {
            for (NetworkInfo networkInfo : networkInfos) {
                if(networkInfo.isConnected()){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 保存状态
     */
    public static final String PREF_NAME = "config";

    public static boolean getBoolean(Context ctx, String key,
                                     boolean defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }
}
