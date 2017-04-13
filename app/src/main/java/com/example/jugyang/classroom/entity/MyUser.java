package com.example.jugyang.classroom.entity;

import cn.bmob.v3.BmobUser;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.entity
 * File Name:        MyUser
 * Description:      Created by jugyang on 4/10/17.
 */

public class MyUser extends BmobUser {

    private int age;
    private boolean sex;
    private String desc;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
