package com.example.jugyang.classroom.entity.course;

import com.example.jugyang.classroom.entity.BaseModel;

/**
 * Created by jugyang on 5/9/17.
 */

public class CourseCommentValue extends BaseModel {
    public String text;
    public String name;
    public String logo;
    public int type;
    public String userId; //评论所属用户ID
}
