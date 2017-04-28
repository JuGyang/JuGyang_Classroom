package com.example.jugyang.classroom.entity;

import com.example.jugyang.classroom.entity.monitor.Monitor;
import com.example.jugyang.classroom.entity.monitor.emevent.EMEvent;

import java.util.ArrayList;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.entity
 * File Name:        AdValue
 * Description:      Created by jugyang on 4/22/17.
 * Function:         function
 */

public class AdValue {
    public String resourceID;
    public String adid;
    public String resource;
    public String thumb;
    public ArrayList<Monitor> startMonitor;
    public ArrayList<Monitor> middleMonitor;
    public ArrayList<Monitor> endMonitor;
    public String clickUrl;
    public ArrayList<Monitor> clickMonitor;
    public EMEvent event;
    public String type;
}
