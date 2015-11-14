package com.shyling.healthmanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.shyling.healthmanager.HealthManagerApplication;
import com.shyling.healthmanager.model.TestRecord;
import com.shyling.healthmanager.model.User;
import com.shyling.healthmanager.util.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mars on 2015/11/9.
 */
public class UserDao {
    private SQLiteDatabase db;
    User user;
    Cursor cursor;
    private final static String table_name = "tb_userinfo";
    private DBHelper helper;
    public UserDao(Context context){
        helper = new DBHelper(context, "tb_userinfo.db", null, 1);
    }
    public void addUser(SQLiteDatabase db, String userName, String passWd, String uName, String birthDay, String cellPhone) {
        //添加用户
        String[] user =new String[]{userName, passWd, uName, birthDay, cellPhone};
        db.execSQL("insert into tb_userinfo values(null,?,?,?,?,?)", user );
    }

    /**
     * 查找用户信息
     * @param userName
     * @return
     */
    public User find(String userName){
        user = new User();
        db = helper.getReadableDatabase();
        cursor = db.query(table_name, null, "userName=?", new String[]{userName}, null, null, null);
        boolean result = cursor.moveToNext();
        //System.out.print("   "+userName);
       if (result){
            user.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
            user.setPassWd(cursor.getString(cursor.getColumnIndex("passWd")));
        }
        cursor.close();
        db.close();

        return user;
    }

}