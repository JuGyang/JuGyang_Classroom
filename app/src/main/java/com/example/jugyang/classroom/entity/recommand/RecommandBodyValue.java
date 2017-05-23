package com.example.jugyang.classroom.entity.recommand;

import com.example.jugyang.classroom.entity.BaseModel;
import com.example.jugyang.classroom.entity.monitor.Monitor;
import com.example.jugyang.classroom.entity.monitor.emevent.EMEvent;

import java.util.ArrayList;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.entity.recommand
 * File Name:        RecommandBodyValue
 * Description:      Created by jugyang on 4/13/17.
 */

public class RecommandBodyValue extends BaseModel {

    public String class_id; //对应课程id
    public int type;
    public String logo;
    public String title;
    public String info;
    public String price;
    public String text;
    public String site;
    public String from;
    public String zan;
    public ArrayList<String> url;
    public String liveUrl;

    //视频专用
    public String thumb;
    public String resource;
    public String resourceID;
    public String adid;
    public ArrayList<Monitor> startMonitor;
    public ArrayList<Monitor> middleMonitor;
    public ArrayList<Monitor> endMonitor;
    public String clickUrl;
    public ArrayList<Monitor> clickMonitor;
    public EMEvent event;
}
