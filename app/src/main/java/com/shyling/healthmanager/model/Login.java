package com.shyling.healthmanager.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.DBHelper;

/**
 * Created by Mars on 2015/11/10.
 */
public class Login {
    private DBHelper dbHelper;
    public Login(User user,Context context, SuccessCallBack successCallBack, FailCallBack failCallBack){
        dbHelper = new DBHelper(context,"tb_userinfo.db",null,1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM "
                + Const.DEVICENAME + " WHERE "
                + Const.USERNAME_COLUMN +  " = ? and "
                + Const.PASSWORD_COLUMN +  " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{user.getUserName(), user.getPassWd()});
        if(cursor!=null){
            if(cursor.moveToFirst()){
                cursor.close();
                db.close();
                dbHelper.close();
                successCallBack.onSuccess();
            }
            else{
                failCallBack.onFail();
            }
        }
    }



    public interface SuccessCallBack{
        void onSuccess();
    }

    public interface FailCallBack{
        void onFail();
    }
}
