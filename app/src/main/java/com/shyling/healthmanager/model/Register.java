package com.shyling.healthmanager.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.DBHelper;


/**
 * Created by Mars on 2015/11/10.
 */
public class Register {
    public Register(User user, Context context,
                    SuccessCallBack successCallBack, FailCallBack failCallBack) {
        DBHelper dbHelper = new DBHelper(context,"health.db",null,1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Const.DEVICENAME,
                null);
        boolean usernameExisted = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int userColumn = cursor
                            .getColumnIndex(Const.USERNAME_COLUMN);
                    String username = cursor.getString(userColumn);
                    if (user.getUserName().equals(username)) {
                        usernameExisted = true;
                        failCallBack.onFail();
                        break;
                    }
                } while (cursor.moveToNext());

                if (!usernameExisted) {
                    ContentValues cv = new ContentValues();
                    cv.put(Const.USERNAME_COLUMN, user.getUserName());
                    cv.put(Const.PASSWORD_COLUMN, user.getPassWd());
                    db.insert(Const.DEVICENAME, null, cv);
                    cursor.close();
                    db.close();
                    dbHelper.close();
                    successCallBack.onSuccess();
                }

            }
        }
    }




    public interface SuccessCallBack {
        void onSuccess();
    }

    public interface FailCallBack {
        void onFail();
    }
}
