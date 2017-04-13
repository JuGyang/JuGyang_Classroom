package com.example.jugyang.classroom.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jugyang.classroom.R;
import com.example.jugyang.classroom.entity.MyUser;

import java.sql.BatchUpdateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.ui
 * File Name:        ForgetPasswordActivity
 * Description:      Created by jugyang on 4/11/17.
 */

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_forget_password;
    private EditText et_email;
    private EditText et_old_password;
    private EditText et_new_password;
    private EditText et_new_password_again;
    private Button btn_update_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initView();
    }

    private void initView() {
        btn_forget_password = (Button) findViewById(R.id.btn_forget_password);
        btn_forget_password.setOnClickListener(this);
        et_email = (EditText) findViewById(R.id.et_email_for_forgetpasswordpage);


        et_old_password = (EditText) findViewById(R.id.et_old_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_new_password_again = (EditText) findViewById(R.id.et_new_password_again);
        btn_update_password = (Button) findViewById(R.id.btn_update_password);
        btn_update_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forget_password:
                final String email = et_email.getText().toString().trim();
                if (!TextUtils.isEmpty(email)) {
                    //send email
                    MyUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if ( e == null) {
                                Toast.makeText(ForgetPasswordActivity.this, "Mail has been send to " + email, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, "Email failed to send!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Input box can not be empty", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_update_password:
                String old_password = et_old_password.getText().toString().trim();
                String new_password = et_new_password.getText().toString().trim();
                String new_password_again = et_new_password_again.getText().toString().trim();
                if (!TextUtils.isEmpty(old_password) & !TextUtils.isEmpty(new_password) & !TextUtils.isEmpty(new_password_again)) {
                    if (new_password.equals(new_password_again)) {
                        MyUser.updateCurrentUserPassword(old_password, new_password, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(ForgetPasswordActivity.this, "Reset password successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, "Reset password failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, "The two entries are inconsistent", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Input box can not be empty", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
