package com.shyling.healthmanager;

import android.app.Application;

import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.DBHelper;

/**
 * Created by shy on 2015/11/8.
 */
public class HealthManagerApplication extends Application {
    public static HealthManagerApplication healthManagerApplication;
    public DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        healthManagerApplication = this;
        dbHelper = new DBHelper(this, Const.DBNAME,null,Const.DBVERSION);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
