package com.shyling.healthmanager;

import android.test.AndroidTestCase;

import com.shyling.healthmanager.dao.CheckUpDAO;
import com.shyling.healthmanager.model.CheckUp;
import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.DBHelper;

/**
 * Created by shyling on 2016/1/29.
 */
public class CheckupValue extends AndroidTestCase {
    DBHelper dbHelper;
    CheckUpDAO checkUpDAO;

    public CheckupValue() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dbHelper = new DBHelper(getContext(), Const.DBNAME, null, Const.DBVERSION);
        checkUpDAO = CheckUpDAO.getInstance(dbHelper);

        CheckUp c1 = new CheckUp();
        c1.setCheckUpDate("昨天");
        c1.setSent(System.currentTimeMillis());
        c1.setDbp(60);
        c1.setSbp(130);
        c1.setHeight(179);
        c1.setWeight(60);
        c1.setPulse(60);
        c1.setUser("username");

        CheckUp c2 = new CheckUp();
        c2.setCheckUpDate("今天");
        c2.setSent(System.currentTimeMillis());
        c2.setDbp(70);
        c2.setSbp(170);
        c2.setHeight(179);
        c2.setWeight(60);
        c2.setPulse(60);
        c2.setUser("username");

        assertTrue(checkUpDAO.add(c1));
        assertTrue(checkUpDAO.add(c2));
    }

    @Override
    protected void tearDown() throws Exception {
//        super.tearDown();
//        checkUpDAO.drop();
//        assertTrue(checkUpDAO.getAll() == null || checkUpDAO.getAll().length == 0);
    }

    public void testFetch() {
        assertEquals(1, checkUpDAO.getAllUnsent().length);
        assertEquals(2, checkUpDAO.getAll().length);
    }
}