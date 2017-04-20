package com.example.jugyang.classroom.zxing.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jugyang.classroom.R;
import com.example.jugyang.classroom.utils.MyLog;
import com.example.jugyang.classroom.zxing.camera.CameraManager;
import com.example.jugyang.classroom.zxing.decoding.CaptureActivityHandler;
import com.example.jugyang.classroom.zxing.decoding.InactivityTimer;
import com.example.jugyang.classroom.zxing.decoding.RGBLuminanceSource;
import com.example.jugyang.classroom.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;

import com.google.zxing.common.HybridBinarizer;

import com.google.zxing.qrcode.QRCodeReader;


import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends AppCompatActivity implements Callback {

    private static final int REQUEST_CODE_SCAN_GALLERY = 100;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ProgressDialog mProgress;
    private Uri photo_path;
    private Bitmap scanBitmap;

    private Button mButtonBack;
    private Button createBtn;
    private Button photoBtn;
    private Button flashBtn;
    private boolean isFlash = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_capture_layout);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_content);

        mButtonBack = (Button) findViewById(R.id.button_back);
        mButtonBack.setOnClickListener(click);
        createBtn = (Button) findViewById(R.id.qrcode_btn);
        createBtn.setOnClickListener(click);
        photoBtn = (Button) findViewById(R.id.photo_btn);
        photoBtn.setOnClickListener(click);
        flashBtn = (Button) findViewById(R.id.flash_btn);
        flashBtn.setOnClickListener(click);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.button_back) {
                MyLog.d("Mark_back");
                finish();
            } else if (id == R.id.flash_btn) {
                if (!isFlash) {
                    CameraManager.get().turnLightOn();
                } else  {
                    CameraManager.get().turnLightOff();
                }
                isFlash = !isFlash;
            } else if (id == R.id.photo_btn) {
                //打开手机中的相册
                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
                innerIntent.setType("image/*");
                Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
                startActivityForResult(wrapperIntent, REQUEST_CODE_SCAN_GALLERY);
            } else if (id == R.id.qrcode_btn) {
                //跳转到生成二维码页面
                finish();
            }
        }
    };




    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        MyLog.d("Mark into");
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
                    //获取选中图片的路径
//                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
//                    if (cursor.moveToFirst()) {
//                        photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                    }
//                    cursor.close();

                    photo_path = data.getData();
                    System.out.println("Classroom " + photo_path);

                    mProgress = new ProgressDialog(CaptureActivity.this);
                    mProgress.setMessage("正在扫描...");
                    mProgress.setCancelable(false);
                    mProgress.show();
                    MyLog.d("Scanning");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Result result = scanningImage(photo_path);
                            if (result != null) {
//                                Message m = handler.obtainMessage();
//                                m.what = R.id.decode_succeeded;
//                                m.obj = result.getText();
//                                handler.sendMessage(m);
//                                Intent resultIntent = new Intent();
//                                Bundle bundle = new Bundle();
//                                bundle.putString("result",result.getText());
//                                bundle.putParcelable("bitmap",result.get);
//                                resultIntent.putExtras(bundle);
//                                CaptureActivity.this.setResult(RESULT_OK, resultIntent);
                                String recode = (result.toString());
                                Intent data = new Intent();
                                data.putExtra("result", recode);
                                setResult(300, data);
                                finish();

                            } else {
//                                Message m = handler.obtainMessage();
//                                m.what = R.id.decode_failed;
//                                m.obj = "Scan failed!";
//                                handler.sendMessage(m);
//                                MyLog.d("Scan failed");
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(), "图片格式有误", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    }).start();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 扫描二维码图片的方法
     * @param path
     * @return
     */
    public Result scanningImage(Uri path) {
        if (path == null || path.equals("")) {
            return null;
        }
        MyLog.d("Mark ScanningImage");
        // DecodeHintType 和EncodeHintType
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
        try {
            MyLog.d("Mark Try");
            Bitmap scanBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photo_path));

            RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
            BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
            QRCodeReader reader = new QRCodeReader();
            System.out.println("Classroom " + bitmap1);
            return reader.decode(bitmap1, hints);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

        //quit the scan view
//		cancelScanButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				CaptureActivity.this.finish();
//			}
//		});
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        //FIXME
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("result", resultString);
            // 不能使用Intent传递大于40kb的bitmap，可以使用一个单例对象存储这个bitmap
//            bundle.putParcelable("bitmap", barcode);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
        }
        CaptureActivity.this.finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

}