package com.example.jugyang.classroom.okHttp.exception;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.okHttp.exception
 * File Name:        OkHttpException
 * Description:      Created by jugyang on 4/12/17.
 */

public class OkHttpException extends Exception {
    private  static final long serialVersionUID = 1L;

    /**
     *  the server return code
     */
    private int ecode;

    /**
     * the server return error message
     */
    private Object emsg;

    public OkHttpException(int ecode, Object emsg) {
        this.ecode = ecode;
        this.emsg = emsg;
    }

    public int getEcode() {
        return ecode;
    }

    public Object getEmsg() {
        return emsg;
    }
}
