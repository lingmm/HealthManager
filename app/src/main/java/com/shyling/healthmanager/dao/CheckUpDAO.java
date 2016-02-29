package com.shyling.healthmanager.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shyling.healthmanager.HealthManagerApplication;
import com.shyling.healthmanager.model.CheckUp;
import com.shyling.healthmanager.util.DBHelper;

import java.util.ArrayList;

/**
 * Created by shy on 2015/11/8.
 */
public class CheckUpDAO {
    private static CheckUpDAO checkUpDAO;
    private SQLiteDatabase sqLiteDatabase;
    private final static String table_name = "checkup";

    private CheckUpDAO(DBHelper dbHelper) {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public static synchronized CheckUpDAO getInstance(DBHelper dbHelper) {
        if (checkUpDAO == null) {
            checkUpDAO = new CheckUpDAO(dbHelper);
        }
        return checkUpDAO;
    }

    public static CheckUpDAO getInstance() {
        return getInstance(HealthManagerApplication.healthManagerApplication.dbHelper);
    }

    //获得所有体检记录
    public CheckUp[] getAll() {
        return getByOptions(null, null);
    }

    //条件查询
    public CheckUp[] getByOptions(String limit, String selection, String... selectionargs) {
        Cursor c = sqLiteDatabase.query(table_name, null, selection, selectionargs, null, null, "id desc", limit);
        if (c == null) {
            return null;
        }
        ArrayList<CheckUp> checkUps = new ArrayList<>();
        while ((c.moveToNext())) {
            CheckUp tmp = new CheckUp();
            tmp.setId(c.getInt(c.getColumnIndex("id")));
            tmp.setCheckUpDate(c.getString(c.getColumnIndex("date")));
            tmp.setHeight(c.getFloat(c.getColumnIndex("height")));
            tmp.setWeight(c.getFloat(c.getColumnIndex("weight")));
            tmp.setSbp(c.getInt(c.getColumnIndex("sbp")));
            tmp.setDbp(c.getInt(c.getColumnIndex("dbp")));
            tmp.setPulse(c.getInt(c.getColumnIndex("pulse")));
            tmp.setUser(c.getInt(c.getColumnIndex("user")));
            tmp.setSent(c.getLong(c.getColumnIndex("sent")));
            checkUps.add(tmp);
        }
        c.close();
        CheckUp[] ret = new CheckUp[checkUps.size()];
        checkUps.toArray(ret);
        return ret;
    }

    //获得所有没有发送到服务器的体检记录
    public CheckUp[] getAllUnsent() {
        return getByOptions(null, "sent>?", "0");
    }

    public boolean add(CheckUp checkUp) {
        ContentValues values = new ContentValues();
        values.put("date", checkUp.getCheckUpDate());
        values.put("height", checkUp.getHeight());
        values.put("weight", checkUp.getWeight());
        values.put("sbp", checkUp.getSbp());
        values.put("dbp", checkUp.getDbp());
        values.put("pulse", checkUp.getPulse());
        values.put("user", checkUp.getUser());
        values.put("sent", checkUp.getSent());

        return sqLiteDatabase.insert(table_name, "id", values) > 0;
    }

    public boolean update(CheckUp checkUp) {
        ContentValues values = new ContentValues();
        values.put("date", checkUp.getCheckUpDate());
        values.put("height", checkUp.getHeight());
        values.put("weight", checkUp.getWeight());
        values.put("sbp", checkUp.getSbp());
        values.put("dbp", checkUp.getDbp());
        values.put("pulse", checkUp.getPulse());
        values.put("user", checkUp.getUser());
        values.put("sent", checkUp.getSent());

        return sqLiteDatabase.update(table_name, values, "id=?", new String[]{String.valueOf(checkUp.getId())}) > 0;
    }

    public boolean delete(int id) {
        return sqLiteDatabase.delete(table_name, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    //删除所有数据
    public void drop() {
        sqLiteDatabase.delete(table_name, null, null);
    }
}
