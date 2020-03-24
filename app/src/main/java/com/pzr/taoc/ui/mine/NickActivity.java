package com.pzr.taoc.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.pzr.taoc.BaseActivity;
import com.pzr.taoc.MineActivity;
import com.pzr.taoc.R;
import com.pzr.taoc.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.pzr.taoc.MainActivity.getStatusBarHeight;

public class NickActivity extends BaseActivity {
    private BaseToolbar mBaseToolbar;
    private EditText mEtNewNick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        BarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_nick;
    }

    @Override
    protected void initView() {
        mBaseToolbar = findViewById(R.id.baseToolbar);
        mEtNewNick = findViewById(R.id.et_new_nick);

        int actionBarHeight = getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, actionBarHeight, 0, 0);//4个参数按顺序分别是左上右下
        mBaseToolbar.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {
        mBaseToolbar.setBackgroundColor(Color.TRANSPARENT);
        mBaseToolbar.setTitle("修改昵称");
        mBaseToolbar.setTitleTextColor(Color.BLACK);
        mBaseToolbar.setBackButton(R.drawable.ic_back);
        mBaseToolbar.addRightText("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNick = mEtNewNick.getText().toString();
                updateUserNick(newNick);

                Intent intent=new Intent();
                intent.putExtra("newNick",newNick);
                setResult(66,intent);
                finish();

            }
        });
    }



    /**
     * 更新用户操作并同步更新本地的用户信息
     *
     * @param nickName
     */
    private void updateUserNick(String nickName) {
        final User user = BmobUser.getCurrentUser(User.class);
        user.setNick(nickName);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShort("修改成功");
//                    Snackbar.make(view, "更新用户信息成功：" + user.getAge(), Snackbar.LENGTH_LONG).show();
                } else {
                    ToastUtils.showShort("修改失败");
//                    Snackbar.make(view, "更新用户信息失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    Log.e("error", e.getMessage());
                }
            }
        });


    }

    @Override
    public void widgetClick(View view) {

    }
}
