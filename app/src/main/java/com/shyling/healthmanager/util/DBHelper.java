package com.shyling.healthmanager.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shy on 2015/11/8.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists test(id integer primary key,time integer not null,height real,weight real,hbp integer,lbp integer,pulse integer,user integer not null)");
        db.execSQL("create table if not exists tb_userinfo(_id integer primary key autoincrement, " +
                "userName varchar(20),userNumber varchar(20), passWd varchar(20), uName varchar(20), birthDay varchar(20), cellPhone varchar(20))");
    }
    public Cursor query()
    {
        //获得SQLiteDatabase实例
        SQLiteDatabase db=getWritableDatabase();
        //查询获得Cursor
        Cursor c=db.query("tb_userinfo", null, null, null, null, null, null,null);
        return c;
    }
    public static void addUser(SQLiteDatabase db, String userName, String userNumber, String passWd, String uName, String birthDay, String cellPhone) {
        //添加用户
        String[] user =new String[]{userName, userNumber, passWd, uName, birthDay, cellPhone};
        db.execSQL("insert into tb_userinfo values(null,?,?,?,?,?,?)", user );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
