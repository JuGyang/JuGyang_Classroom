package com.example.jugyang.classroom.bmobpush;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.jugyang.classroom.R;
import com.example.jugyang.classroom.utils.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;

/**
 * Created by jugyang on 5/11/17.
 */

public class MyPushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            //收到消息进行一些处理：发送一个通知
            String jsonStr = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            String content = null;
            //处理Json的数据，将其转化为正常string类型
            try {
                JSONObject jsonObject=new JSONObject(jsonStr);
                content=jsonObject.getString("alert");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            NotificationManager manager= (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Notification notification= new Notification.Builder(context).
                    setSmallIcon(R.mipmap.ic_launcher).//设置推送消息的图标
                    setContentTitle("您收到一条推送信息！") .//设置推送标题
                    setContentText(content).build();//设置推送内容
            manager.notify(1,notification);//设置通知栏
            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
        }
    }
}
