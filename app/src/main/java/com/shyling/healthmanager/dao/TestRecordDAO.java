package com.shyling.healthmanager.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shyling.healthmanager.HealthManagerApplication;
import com.shyling.healthmanager.model.TestRecord;

import java.util.ArrayList;

/**
 * Created by shy on 2015/11/8.
 */
public class TestRecordDAO {
    private static TestRecordDAO testRecordDAO;
    private SQLiteDatabase sqLiteDatabase;
    private final static String table_name = "test";

    private TestRecordDAO(){
        sqLiteDatabase = HealthManagerApplication.healthManagerApplication.dbHelper.getReadableDatabase();
    }

    public static synchronized TestRecordDAO getInstance(){
        if(testRecordDAO==null){
            testRecordDAO = new TestRecordDAO();
        }
        return testRecordDAO;
    }

    public TestRecord[] getAll(){
        Cursor c = sqLiteDatabase.rawQuery("select * from ?",new String[]{table_name});
        if(c.moveToFirst()){
            ArrayList<TestRecord> testRecords = new ArrayList<>();
            while (c.moveToNext()){
                TestRecord tmp = new TestRecord();
                tmp.setId(c.getInt(0));
                tmp.setTime(c.getLong(1));
                tmp.setHeight(c.getFloat(2));
                tmp.setWeight(c.getFloat(3));
                tmp.setHbp(c.getInt(4));
                tmp.setLbp(c.getInt(5));
                tmp.setPulse(c.getInt(6));
                tmp.setUser(c.getInt(7));
                testRecords.add(tmp);
            }
            c.close();
            TestRecord[] ret = new TestRecord[testRecords.size()];
            testRecords.toArray(ret);
            return ret;
        }else {
            c.close();
            return null;
        }
    }

    public boolean save(TestRecord testRecord){
        ContentValues values = new ContentValues();
        values.put("id",testRecord.getId());
        values.put("time",testRecord.getTime());
        values.put("height",testRecord.getHeight());
        values.put("weight",testRecord.getWeight());
        values.put("hbp",testRecord.getHbp());
        values.put("lbp",testRecord.getLbp());
        values.put("pulse",testRecord.getPulse());
        values.put("user",testRecord.getUser());

        return sqLiteDatabase.insert(table_name,null,values) > 0;
    }
}
