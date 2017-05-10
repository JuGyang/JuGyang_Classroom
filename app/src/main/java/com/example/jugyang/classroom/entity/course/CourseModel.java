package com.example.jugyang.classroom.entity.course;

import com.example.jugyang.classroom.entity.BaseModel;

import java.util.ArrayList;

/**
 * Created by jugyang on 5/9/17.
 */

public class CourseModel extends BaseModel {

    public CourseHeaderValue head;
    public CourseFooterValue footer;
    public ArrayList<CourseCommentValue> body;
}
