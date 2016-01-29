package com.shyling.healthmanager.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库访问帮助类
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    //数据库生成时系统调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS checkup(id INTEGER PRIMARY KEY AUTOINCREMENT,date VARCHAR(20)" +
                " NOT NULL,height REAL,weight REAL,sbp INTEGER,dbp INTEGER,pulse INTEGER,user VARCHAR(20) not null,sent integer default 0)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_userinfo(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userName VARCHAR(20),userNumber VARCHAR(20), passWd VARCHAR(20), uName VARCHAR(20), birthDay VARCHAR(20), cellPhone VARCHAR(20))");
    }

    //版本升级时系统自动调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
