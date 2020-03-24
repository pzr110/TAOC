package com.pzr.taoc;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pzr.taoc.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity {

    private EditText mEtAccount;
    private EditText mEtPass;
    private TextView mTvLogin;
    private TextView mTvRegister;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.WHITE);
        BarUtils.setStatusBarLightMode(this, true);

        boolean main = SPUtils.getInstance().getBoolean("main");
        if (main){
            ActivityUtils.startActivity(MainActivity.class);
            finish();
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mEtAccount = findViewById(R.id.et_account);
        mEtPass = findViewById(R.id.et_pass);
        mTvLogin = findViewById(R.id.tv_login);
        mTvLogin.setOnClickListener(this);

        mTvRegister = findViewById(R.id.tv_register);
        mTvRegister.setOnClickListener(this);
    }



    /**
     * 账号密码登录
     */
    private void login(String account, String pass) {
        final User user = new User();
        //此处替换为你的用户名
        user.setUsername(account);
        //此处替换为你的密码
        user.setPassword(pass);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User bmobUser, BmobException e) {
                if (e == null) {
                    User user = BmobUser.getCurrentUser(User.class);
                    ActivityUtils.startActivity(MainActivity.class);
                    ToastUtils.showShort("登录成功");
                    SPUtils.getInstance().put("main",true);
                    ActivityUtils.finishAllActivities();
//                    Snackbar.make(view, "登录成功：" + user.getUsername(), Snackbar.LENGTH_LONG).show();
                } else {
                    ToastUtils.showShort("登录失败");
//                    Snackbar.make(view, "登录失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    protected void initData() {

    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login: {
                login(mEtAccount.getText().toString(), mEtPass.getText().toString());
                break;
            }
            case R.id.tv_register: {
                ActivityUtils.startActivity(SignUpActivity.class);
            }
        }
    }


}
