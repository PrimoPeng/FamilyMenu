package com.example.android.family.menu.application;

import android.app.Application;

import com.example.android.family.menu.db.FamilyMenuDbHelper;


/**
 * Created by Administrator on 2016/9/7.
 */
public class FamilyMenuApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FamilyMenuDbHelper familyMenuDbHelper = FamilyMenuDbHelper.getInstance(this);
        familyMenuDbHelper.onCreate(familyMenuDbHelper.getWritableDatabase());
    }
}
