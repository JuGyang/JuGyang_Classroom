package com.example.jugyang.classroom.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jugyang.classroom.MainActivity;
import com.example.jugyang.classroom.R;
import com.example.jugyang.classroom.entity.MyUser;
import com.example.jugyang.classroom.utils.ShareUtils;
import com.example.jugyang.classroom.view.CustomDialog;

import org.w3c.dom.Text;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscription;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.keepScreenOn;
import static android.R.attr.onClick;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.ui
 * File Name:        LoginActivity
 * Description:      Created by jugyang on 4/10/17.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_register;
    private Button btn_login;
    private EditText et_name;
    private EditText et_password;
    private CheckBox check_keep_password;
    private TextView tv_forget;

    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        check_keep_password = (CheckBox) findViewById(R.id.check_keep_password);

        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);


        dialog = new CustomDialog(this, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                R.layout.dialog_loading, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);

        //Set state
        //Default false
        boolean isCheck = ShareUtils.getBoolean(this, "keep_password", false);
        check_keep_password.setChecked(isCheck);
        if (isCheck) {
            et_name.setText(ShareUtils.getString(this, "name", ""));
            et_password.setText(ShareUtils.getString(this, "password", ""));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btn_login:
                String name = et_name.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(password)) {

                    dialog.show();
                    final MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(password);
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            dialog.dismiss();
                            if (e == null) {
                                //判断邮箱是否验证
                                if (user.getEmailVerified()) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please go to email verification", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Login failed" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Input box can not be empty", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Save state
        ShareUtils.putBoolean(this, "keep_password", check_keep_password.isChecked());

        if (check_keep_password.isChecked()) {
            //Remember username and password
            ShareUtils.putString(this, "name", et_name.getText().toString().trim());
            ShareUtils.putString(this, "password", et_password.getText().toString().trim());
        } else {
            ShareUtils.deleShare(this, "name");
            ShareUtils.deleShare(this, "password");
        }
    }
}
