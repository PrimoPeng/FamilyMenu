package com.example.android.family.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewStub;

/**
 * Created by Administrator on 2016/9/7.
 */
public class BaseToolbarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void setContentView(int layoutResID) {
        ViewStub vs = (ViewStub) findViewById(R.id.layout_root_content);
        vs.setLayoutResource(layoutResID);
        vs.inflate();
    }
}
