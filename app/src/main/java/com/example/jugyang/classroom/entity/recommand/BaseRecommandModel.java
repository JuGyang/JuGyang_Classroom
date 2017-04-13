package com.example.jugyang.classroom.entity.recommand;

import com.example.jugyang.classroom.entity.BaseModel;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.entity.recommand
 * File Name:        BaseRecommandModel
 * Description:      Created by jugyang on 4/13/17.
 */

public class BaseRecommandModel extends BaseModel {

    //保证我们变量名与json的key完全一样
    public String ecode;
    public String emsg;
    public RecommandModel data;
}
