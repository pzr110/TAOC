package com.pzr.taoc.ui.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.pzr.taoc.BaseActivity;
import com.pzr.taoc.R;

import static com.pzr.taoc.MainActivity.getStatusBarHeight;

public class MessageActivity extends BaseActivity {
    private BaseToolbar mBaseToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        BarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_message;
    }

    @Override
    protected void initView() {
        mBaseToolbar = findViewById(R.id.baseToolbar);


        int actionBarHeight = getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, actionBarHeight, 0, 0);//4个参数按顺序分别是左上右下
        mBaseToolbar.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {
        mBaseToolbar.setBackgroundColor(Color.TRANSPARENT);
        mBaseToolbar.setTitle("我的消息");
        mBaseToolbar.setTitleTextColor(Color.BLACK);
        mBaseToolbar.setBackButton(R.drawable.ic_back);
    }

    @Override
    public void widgetClick(View view) {

    }
}
