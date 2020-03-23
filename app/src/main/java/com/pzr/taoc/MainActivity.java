package com.pzr.taoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.mirkowu.basetoolbar.BaseToolbar;

import java.io.File;

public class MainActivity extends BaseActivity {

    private BaseToolbar mBaseToolbar;
    private Switch mSwithTranslate;
    private TextView mTvTranslate;

    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private AssetManager mAssetManager;
    private ImageView mIvPlay;

    private Spinner mSpinnerSpeed;
    private double mSpeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        BarUtils.setStatusBarLightMode(this, true);

        mAssetManager = getResources().getAssets();
        //权限判断，如果没有权限就请求权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initMediaPlayer();//初始化播放器 MediaPlayer
        }

    }

    private void setPlayerSpeed(float speed) {
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.M) {
            PlaybackParams playbackParams = mMediaPlayer.getPlaybackParams();
            playbackParams.setSpeed(speed);
            mMediaPlayer.setPlaybackParams(playbackParams);
        }
    }

    private void initMediaPlayer() {
        try {
//            File file = new File(Environment.getExternalStorageDirectory(), "music.mp3");
            AssetFileDescriptor fileDescriptor = mAssetManager.openFd("01.mp3");
            mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getStartOffset());//指定音频文件路径
            mMediaPlayer.setLooping(true);//设置为循环播放
            mMediaPlayer.prepare();//初始化播放器MediaPlayer

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMediaPlayer();
                } else {
                    Toast.makeText(this, "拒绝权限，将无法使用程序。", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mBaseToolbar = findViewById(R.id.baseToolbar);
        mSwithTranslate = findViewById(R.id.swith_translate);
        mSwithTranslate.setChecked(true);
        mTvTranslate = findViewById(R.id.tv_translate);
        mIvPlay = findViewById(R.id.iv_play);
        mIvPlay.setOnClickListener(this);
        mSpinnerSpeed = findViewById(R.id.spinner_speed);
        mSpinnerSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
//                    case 0:
                    case 2: {
                        mSpeed = 1;
                        setPlayerSpeed(1);
                        Play();
                        break;
                    }
                    case 1: {
                        setPlayerSpeed((float) 0.5);
                        Play();
                        break;
                    }
                    case 3: {
                        mSpeed = 1.5;
                        setPlayerSpeed((float) 1.5);
                        Play();
                        break;
                    }
                    case 4: {
                        mSpeed = 2;
                        setPlayerSpeed((float) 2);
                        Play();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        initViewInfo();

        // git test

    }

    private void Play(){
        if (mMediaPlayer.isPlaying()) {
//            mMediaPlayer.start();
            mIvPlay.setBackgroundResource(R.drawable.ic_pause);
        } else {
//            mMediaPlayer.pause();
            mIvPlay.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private void initViewInfo() {
        int actionBarHeight = getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, actionBarHeight, 0, 0);//4个参数按顺序分别是左上右下
        mBaseToolbar.setLayoutParams(layoutParams);

        mSwithTranslate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ToastUtils.showShort("开启");
                    mTvTranslate.setVisibility(View.VISIBLE);
                } else {
                    ToastUtils.showShort("关闭");
                    mTvTranslate.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    @Override
    protected void initData() {
        // ToolBar
        mBaseToolbar.setBackgroundColor(Color.TRANSPARENT);
        mBaseToolbar.setTitle("学而第一");
        mBaseToolbar.setTitleTextColor(Color.BLACK);
        mBaseToolbar.addLeftImage(R.drawable.ic_catalog, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("目录");
            }
        });

        mBaseToolbar.addRightImage(R.drawable.ic_user, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("个人中心");
            }
        });


    }


    @Override
    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play: {
                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                    mIvPlay.setBackgroundResource(R.drawable.ic_pause);
                } else {
                    mMediaPlayer.pause();
                    mIvPlay.setBackgroundResource(R.drawable.ic_play);
                }
                break;
            }
        }
    }
}
