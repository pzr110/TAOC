package com.pzr.taoc;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.pzr.taoc.bean.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends BaseActivity {

    private EditText mEtAccount;
    private EditText mEtPass;
    private EditText mEtNick;
    private TextView mTvRegister;

    private BaseToolbar mBaseToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.WHITE);
        BarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_sign;
    }

    @Override
    protected void initView() {
        mEtAccount = findViewById(R.id.et_account);
        mEtPass = findViewById(R.id.et_pass);
        mEtNick = findViewById(R.id.et_nick);
        mTvRegister = findViewById(R.id.tv_register);
        mTvRegister.setOnClickListener(this);
        mBaseToolbar = findViewById(R.id.baseToolbar);

    }


    @Override
    protected void initData() {
        mBaseToolbar.setStatusBarTransparent();
//        mBaseToolbar.setBackgroundResource(R.mipmap.top_bg);
        mBaseToolbar.setBackButton(R.drawable.ic_back);
        mBaseToolbar.setTitle("注册账号");
        mBaseToolbar.setTitleTextColor(Color.BLACK);
    }

    @Override
    public void widgetClick(View view) {
        if (view.getId() == R.id.tv_register) {
            signUp(mEtAccount.getText().toString(), mEtPass.getText().toString(), mEtNick.getText().toString());
        }
    }

    /**
     * 账号密码注册
     *
     * @param account
     * @param pass
     * @param nick
     */
    private void signUp(String account, String pass, String nick) {
        final User user = new User();
        user.setUsername(account);
        user.setPassword(pass);
        user.setNick(nick);
//        user.setAge(18);
//        user.setGender(0);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
//                    Snackbar.make(view, "注册成功", Snackbar.LENGTH_LONG).show();
                    ToastUtils.showShort("注册成功");
                    ActivityUtils.startActivity(LoginActivity.class);
                    finish();
                } else {
                    ToastUtils.showShort("未注册");
//                    Snackbar.make(view, "尚未失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

}
