package com.example.jugyang.classroom.entity.recommand;

import com.example.jugyang.classroom.entity.BaseModel;

import java.util.ArrayList;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.entity.recommand
 * File Name:        RecommandHeadValue
 * Description:      Created by jugyang on 4/13/17.
 */

public class RecommandHeadValue extends BaseModel{

    public ArrayList<String> ads;
    public ArrayList<String> middle;
    public ArrayList<RecommandFooterValue> footer;
}
