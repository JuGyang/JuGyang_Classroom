package com.example.jugyang.classroom.entity;

import java.net.URL;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

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
    private BmobFile image;
    private String url;

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

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImageUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return url;
    }
}
