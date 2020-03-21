package com.pzr.taoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.mirkowu.basetoolbar.BaseToolbar;

public class MainActivity extends BaseActivity {

    private BaseToolbar mBaseToolbar;
    private Switch mSwithTranslate;
    private TextView mTvTranslate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        BarUtils.setStatusBarLightMode(this, true);
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

        initViewInfo();

        // git test

    }

    private void initViewInfo() {
        int actionBarHeight = getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, actionBarHeight, 0, 0);//4个参数按顺序分别是左上右下
        mBaseToolbar.setLayoutParams(layoutParams);

        mSwithTranslate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ToastUtils.showShort("开启");
                    mTvTranslate.setVisibility(View.VISIBLE);
                }else {
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

    }
}
