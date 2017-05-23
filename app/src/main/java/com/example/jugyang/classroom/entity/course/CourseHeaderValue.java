package com.example.jugyang.classroom.entity.course;

import com.example.jugyang.classroom.entity.AdValue;
import com.example.jugyang.classroom.entity.BaseModel;

import java.util.ArrayList;

/**
 * Created by jugyang on 5/9/17.
 */

public class CourseHeaderValue extends BaseModel {
    public ArrayList<String> photoUrls;
    public String text;
    public String name;
    public String logo;
    public String oldPrice;
    public String newPrice;
    public String zan;
    public String scan;
    public String hotComment;
    public String from;
    public String dayTime;
    public AdValue video;
    public int qq_num1;
    public int qq_num2;
    public int qq_num3;
}
