package com.example.jugyang.classroom.entity.recommand;

import com.example.jugyang.classroom.entity.BaseModel;

import java.util.ArrayList;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.entity.recommand
 * File Name:        RecommandModel
 * Description:      Created by jugyang on 4/13/17.
 */

public class RecommandModel extends BaseModel {

    /**
     * 分别对应我们json中的两个数据部分
     */
    public ArrayList<RecommandBodyValue> list;
    public RecommandHeadValue head;
}
