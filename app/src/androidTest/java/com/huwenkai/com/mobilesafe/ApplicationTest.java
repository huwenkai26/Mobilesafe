package com.huwenkai.com.mobilesafe;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.huwenkai.com.mobilesafe.db.BlacknumberDao.BlackNumberDao;
import com.huwenkai.com.mobilesafe.db.Domian.BlackNumberInfo;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void query() {
        BlackNumberDao blackNumberDao = BlackNumberDao.getInstances(getContext());
        List<BlackNumberInfo> findall = blackNumberDao.findall();
    }

    public void inset() {
        BlackNumberDao blackNumberDao = BlackNumberDao.getInstances(getContext());
        blackNumberDao.insert("110", "0");
    }
}