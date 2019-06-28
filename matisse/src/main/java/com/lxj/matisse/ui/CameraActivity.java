package com.lxj.matisse.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.cjt2325.cameralibrary.util.FileUtil;
import com.lxj.matisse.CaptureMode;
import com.lxj.matisse.MatisseConst;
import com.lxj.matisse.R;
import com.lxj.matisse.internal.entity.SelectionSpec;
import com.yalantis.ucrop.UCrop;
import java.io.File;

public class CameraActivity extends AppCompatActivity {
    private JCameraView jCameraView;
    private SelectionSpec mSpec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSpec = SelectionSpec.getInstance();
        setTheme(mSpec.themeId);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera);
        jCameraView = findViewById(R.id.jcameraview);
        init();
    }

    private int getFeature(){
        if(SelectionSpec.getInstance().captureMode== CaptureMode.All){
            return JCameraView.BUTTON_STATE_BOTH;
        }else if(SelectionSpec.getInstance().captureMode== CaptureMode.Image){
            return JCameraView.BUTTON_STATE_ONLY_CAPTURE;
        }else {
            return JCameraView.BUTTON_STATE_ONLY_RECORDER;
        }
    }

    private void init(){
        //设置视频保存路径
        jCameraView.setSaveVideoPath(getCacheDir() + File.separator + "matisse");
        jCameraView.setFeatures(getFeature());
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
            @Override
            public void AudioPermissionError() {
                Toast.makeText(CameraActivity.this, "没有录音权限", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
        //JCameraView监听
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                String path = FileUtil.saveBitmap("matisse", bitmap);
                if(mSpec.isCrop){
                    //需要裁剪
                    MatisseActivity.startCrop(CameraActivity.this, Uri.parse("file://"+path));
                }else {
                    Intent intent = new Intent();
                    intent.putExtra(MatisseConst.EXTRA_RESULT_CAPTURE_IMAGE_PATH, path);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                String path = FileUtil.saveBitmap("matisse", firstFrame);
//                Log.i("CJT", "url = " + url + ", Bitmap = " + path);
                Intent intent = new Intent();
                intent.putExtra(MatisseConst.EXTRA_RESULT_CAPTURE_IMAGE_PATH, path);
                intent.putExtra(MatisseConst.EXTRA_RESULT_CAPTURE_VIDEO_PATH, url);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                //finish with result.
                Intent result = new Intent();
                result.putExtra(MatisseConst.EXTRA_RESULT_CROP_PATH, resultUri.getPath());
                setResult(RESULT_OK, result);
                finish();
            } else {
                Log.e("Matisse", "ucrop occur error: "+UCrop.getError(data).toString());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }
}
