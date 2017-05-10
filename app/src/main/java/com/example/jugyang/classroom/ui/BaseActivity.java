package com.example.jugyang.classroom.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Projeck Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.ui
 * File Name:        BaseActivity
 * Description:      Created by jugyang on 4/9/17.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Show Back Icon
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //Menu Bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
