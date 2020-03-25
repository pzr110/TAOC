package com.pzr.taoc.ui.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.snackbar.Snackbar;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.pzr.taoc.BaseActivity;
import com.pzr.taoc.LoginActivity;
import com.pzr.taoc.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.pzr.taoc.MainActivity.getStatusBarHeight;

public class SetPassActivity extends BaseActivity {
    private BaseToolbar mBaseToolbar;
    private EditText mEyOldPass;
    private EditText mEtNewPass;
    private EditText mEtConfirmPass;
    private TextView mTvSubmit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        BarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_pass;
    }

    @Override
    protected void initView() {
        mBaseToolbar = findViewById(R.id.baseToolbar);
        mEyOldPass = findViewById(R.id.ey_old_pass);
        mEtNewPass = findViewById(R.id.et_new_pass);
        mEtConfirmPass = findViewById(R.id.et_confirm_pass);
        mTvSubmit = findViewById(R.id.tv_submit);
        mTvSubmit.setOnClickListener(this);

        int actionBarHeight = getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, actionBarHeight, 0, 0);//4个参数按顺序分别是左上右下
        mBaseToolbar.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {

        mBaseToolbar.setBackgroundColor(Color.TRANSPARENT);
        mBaseToolbar.setTitle("修改密码");
        mBaseToolbar.setTitleTextColor(Color.BLACK);
        mBaseToolbar.setBackButton(R.drawable.ic_back);

    }

    @Override
    public void widgetClick(View view) {
        if (view.getId() == R.id.tv_submit) {
            if (mEyOldPass.getText().toString().equals("") ||
                    mEtNewPass.getText().toString().equals("") ||
                    mEtConfirmPass.getText().toString().equals("")) {
                ToastUtils.showShort("输入不能为空");
            } else if (!mEtConfirmPass.getText().toString().equals(mEtNewPass.getText().toString())) {
                ToastUtils.showShort("两次新密码不一致");
            } else {
                setNewPass(mEyOldPass.getText().toString(), mEtNewPass.getText().toString());
            }
        }
    }

    private void setNewPass(String oldPass, String newPass) {

        BmobUser.updateCurrentUserPassword(oldPass, newPass, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShort("修改成功");
                    SPUtils.getInstance().put("main",false);
                    ActivityUtils.startActivity(LoginActivity.class);
//                    ActivityUtils.finishAllActivities();
//                    Snackbar.make(view, "查询成功", Snackbar.LENGTH_LONG).show();
                } else {
                    ToastUtils.showShort("修改失败");
//                    Snackbar.make(view, "查询失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
