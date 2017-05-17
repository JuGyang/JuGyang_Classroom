package com.example.jugyang.classroom.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jugyang.classroom.ImagerLoader.ImageLoaderManager;
import com.example.jugyang.classroom.R;
import com.example.jugyang.classroom.entity.MyUser;
import com.example.jugyang.classroom.utils.MyLog;
import com.example.jugyang.classroom.utils.ShareUtils;
import com.example.jugyang.classroom.utils.StaticClass;
import com.example.jugyang.classroom.view.CustomDialog;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.ui
 * File Name:        PersonalCenterActivity
 * Description:      Created by jugyang on 4/11/17.
 */

public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_log_out_user;
    private TextView edit_user;

    private EditText et_username;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_desc;

    private Button btn_modify;

    private CircleImageView profile_image;

    private CustomDialog dialog;

    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;

    private File tempFile = null;

    private Bitmap avatar_bitmap;


    private String path; //本地头像地址

    //BmobFile bmobFile;

    MyUser user = new MyUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        initView();
    }

    private void initView() {
        btn_log_out_user = (Button) findViewById(R.id.btn_log_out_user);
        btn_log_out_user.setOnClickListener(this);

        edit_user = (TextView) findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);

        et_username = (EditText) findViewById(R.id.et_username);
        et_sex = (EditText) findViewById(R.id.et_sex);
        et_age = (EditText) findViewById(R.id.et_age);
        et_desc = (EditText) findViewById(R.id.et_desc);

        btn_modify = (Button) findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(this);

        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);



        //拿到String
//        String imgString = ShareUtils.getString(this, "image_title", "");
//        if (!imgString.equals("")) {
//            //利用Base64将我们的string转换
//            byte [] byteArray = Base64.decode(imgString, Base64.DEFAULT);
//            ByteArrayInputStream byStream = new ByteArrayInputStream(byteArray);
//            //生产bitmap
//            Bitmap bitmap = BitmapFactory.decodeStream(byStream);
//            profile_image.setImageBitmap(bitmap);
//        }




        //初始化
        dialog = new CustomDialog(this, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);
        //提示框以外点击无效
        dialog.setCancelable(false);

        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        //默认是不可点击的
        setEnabled(false);

        //设置具体的值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_username.setText(userInfo.getUsername());
        et_sex.setText(userInfo.isSex() ? "Male" : "Female");
        et_age.setText(userInfo.getAge() + "");
        et_desc.setText(userInfo.getDesc());


//        Bitmap avater_bitmap = returnBitmap(avater_url);
//        profile_image.setImageBitmap(avater_bitmap);

        //头像问题GG很烦
//        if (true) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
//                    String avatar_url = userInfo.getImageUrl();
//                    avatar_bitmap = returnBitmap(avatar_url);
//                    MyLog.d("Avatar Url: " + avatar_url);
//                    //profile_image.setImageBitmap(avatar_bitmap);
//                }
//            }).start();
//
//            profile_image.setImageBitmap(avatar_bitmap);
//        }

        String avatar_url = userInfo.getImageUrl();
        ImageLoaderManager imageLoaderManager;
        imageLoaderManager = ImageLoaderManager.getInstance(this);
        imageLoaderManager.displayImage(profile_image, avatar_url);

    }

    //Control Visible or Gone
    private void setEnabled(boolean bool) {
        et_username.setEnabled(bool);
        et_sex.setEnabled(bool);
        et_age.setEnabled(bool);
        et_desc.setEnabled(bool);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_log_out_user:
                //Log out
                MyUser.logOut();
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.edit_user:
                MyLog.d("edit_user");
                setEnabled(true);
                btn_modify.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_modify:
                String username = et_username.getText().toString();
                String age = et_age.getText().toString();
                String sex = et_sex.getText().toString();
                String desc = et_desc.getText().toString();

                if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(age) &!TextUtils.isEmpty(sex)) {
                    //Update properties
                    //MyUser user = new MyUser();
                    user.setUsername(username);
                    user.setAge(Integer.parseInt(age));
                    if (sex.equals("Male")) {
                        user.setSex(true);
                    } else {
                        user.setSex(false);
                    }

                    if (!TextUtils.isEmpty(desc)) {
                        user.setDesc(desc);
                    } else {
                        user.setDesc("How lazy is this person! Nothing left.");
                    }

                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                setEnabled(false);
                                btn_modify.setVisibility(View.GONE);
                                Toast.makeText(PersonalCenterActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PersonalCenterActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Input box can not be empty", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }


    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, StaticClass.IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用的话就进行存储
        //Android N 后此方法失效，后续处理
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
//                        StaticClass.PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, StaticClass.CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != this.RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case StaticClass.IMAGE_REQUEST_CODE:
                    Uri uri = data.getData();
                    path = getImagePath(uri, null);
                    startPhotoZoom(uri);
                    break;
                //相机数据
                case StaticClass.CAMERA_REQUEST_CODE:
                    //Android N 后此方法失效
//                    tempFile = new File(Environment.getExternalStorageDirectory(),
//                            StaticClass.PHOTO_IMAGE_FILE_NAME);
//                    startPhotoZoom(Uri.fromFile(tempFile));
                    //暂时这样解决，以后再说
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap bitmap = bundle.getParcelable("data");
                        profile_image.setImageBitmap(bitmap);
                    }
                    break;
                case StaticClass.RESULT_REQUEST_CODE:
                    if (data != null) {
                        setImageToView(data);
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    //裁剪照片
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            MyLog.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, StaticClass.RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            final BmobFile bmobFile = new BmobFile(new File(path));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    user.setImage(bmobFile);
                    MyLog.e("Mark setImage");
                    Toast.makeText(PersonalCenterActivity.this, "Update Image", Toast.LENGTH_SHORT).show();
                    String url = bmobFile.getFileUrl();
                    user.setImageUrl(url);
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(PersonalCenterActivity.this, "Update User Avatar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            });
            Bitmap bitmap = bundle.getParcelable("data");
            profile_image.setImageBitmap(bitmap);
        }
//        Uri uri = data.getData();
//        path = getImagePath(uri, null);
//        final BmobFile bmobFile = new BmobFile(new File(path));
//        bmobFile.uploadblock(new UploadFileListener() {
//            @Override
//            public void done(BmobException e) {
//                user.setImage(bmobFile);
//                user.update(user.getObjectId(), new UpdateListener() {
//                    @Override
//                    public void done(BmobException e) {
//                        if (e == null) {
//                            Toast.makeText(PersonalCenterActivity.this, "update", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
//        ContentResolver cr = this.getContentResolver();
//        try {
//            MyLog.e(path.toString());
//            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//            /**
//             * 将Bitmap设定到ImageView
//             */
//            profile_image.setImageBitmap(bitmap);
//
//        } catch (FileNotFoundException e) {
//            MyLog.e(e.getMessage());
//            e.printStackTrace();
//        }
    }

    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();

        }
        return path;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        //保存
//        BitmapDrawable drawable = (BitmapDrawable) profile_image.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
//        //将Bitmap压缩成字节数组输出流
//        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byStream);
//        //利用Base64将字节数组输出流转换成String
//        byte [] byteArray = byStream.toByteArray();
//        String imgString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
//        //将String保存shareUtils
//        ShareUtils.putString(this, "image_title", imgString);
    }

    public Bitmap returnBitmap(String urlpath) {
        Bitmap map = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            map = BitmapFactory.decodeStream(in);
            // TODO Auto-generated catch block
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
