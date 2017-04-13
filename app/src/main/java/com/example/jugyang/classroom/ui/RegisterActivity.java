package com.example.jugyang.classroom.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jugyang.classroom.R;
import com.example.jugyang.classroom.entity.MyUser;

import java.sql.BatchUpdateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.ui
 * File Name:        RegisterActivity
 * Description:      Created by jugyang on 4/10/17.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_user;
    private EditText et_age;
    private EditText et_desc;
    private RadioGroup mRadioGroup;
    private EditText et_password_one;
    private EditText et_password_two;
    private EditText et_email;
    private Button btn_register_for_registerpage;

    private boolean isGender = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        et_user = (EditText) findViewById(R.id.et_user);
        et_age = (EditText) findViewById(R.id.et_age);
        et_desc = (EditText) findViewById(R.id.et_desc);
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        et_password_one = (EditText) findViewById(R.id.et_password_one);
        et_password_two = (EditText) findViewById(R.id.et_password_two);
        et_email = (EditText) findViewById(R.id.et_email);
        btn_register_for_registerpage = (Button) findViewById(R.id.btn_register_for_registerpage);
        btn_register_for_registerpage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register_for_registerpage:
                //Get Edit Value
                String name = et_user.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();
                String password_one = et_password_one.getText().toString().trim();
                String password_two = et_password_two.getText().toString().trim();
                String email = et_email.getText().toString().trim();

                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(age) &
                        !TextUtils.isEmpty(password_one) &
                        !TextUtils.isEmpty(password_two) &
                        !TextUtils.isEmpty(email)
                        ) {

                    //Determine Password
                    if (password_one.equals(password_two)) {
                        //Determine Sex
                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (checkedId == R.id.rb_male) {
                                    isGender = true;
                                } else if (checkedId == R.id.rb_female) {
                                    isGender = false;
                                }

                            }
                        });

                        //Determine Desc
                        if (TextUtils.isEmpty(desc)) {
                            desc = "How lazy is this person! Nothing left.";
                        }

                        //Register
                        MyUser user = new MyUser();
                        user.setUsername(name);
                        user.setPassword(password_one);
                        user.setEmail(email);
                        user.setAge(Integer.parseInt(age));
                        user.setSex(isGender);
                        user.setDesc(desc);

                        user.signUp(new SaveListener<MyUser>() {
                            @Override
                            public void done(MyUser myUser, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Registration failed" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else  {
                        Toast.makeText(this, "The password entered twice is inconsistent", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Input box can not be empty", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
