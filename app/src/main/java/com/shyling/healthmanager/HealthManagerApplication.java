package com.shyling.healthmanager;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.DBHelper;

import java.util.Iterator;
import java.util.List;

/**
 * Created by shy on 2015/11/8.
 */
public class HealthManagerApplication extends Application {
    public static HealthManagerApplication healthManagerApplication;
    public DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果app启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
        // name就立即返回

        if (processAppName == null
                || !processAppName.equalsIgnoreCase("com.shyling.healthmanager")) {
            Log.e("health", "enter the service process!");
            // "com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

//        初始化环信sdk
        EMChat.getInstance().init(getApplicationContext());
        initHXOptions();
        healthManagerApplication = this;
        dbHelper = new DBHelper(this, Const.DBNAME,null,Const.DBVERSION);
    }

    protected void initHXOptions() {
        // 获取到EMChatOptions对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // 默认添加好友时，是不需要验证的true，改成需要验证false
        options.setAcceptInvitationAlways(false);
        // 默认环信是不维护好友关系列表的，如果app依赖环信的好友关系，把这个属性设置为true
        options.setUseRoster(true);
        options.setNumberOfMessagesLoaded(1);
    }


    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm
                            .getApplicationInfo(info.processName,
                                    PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
