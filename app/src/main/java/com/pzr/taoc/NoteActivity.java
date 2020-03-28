package com.pzr.taoc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.snackbar.Snackbar;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.pzr.taoc.bean.DataBean;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.pzr.taoc.MainActivity.getStatusBarHeight;

public class NoteActivity extends BaseActivity {

    private BaseToolbar mBaseToolbar;
    private EditText mEtNote;
    private TextView mTvNote;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        BarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_note;
    }

    @Override
    protected void initView() {
        mBaseToolbar = findViewById(R.id.baseToolbar);
        mEtNote = findViewById(R.id.et_note);
        mTvNote = findViewById(R.id.tv_note);


        int actionBarHeight = getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, actionBarHeight, 0, 0);//4个参数按顺序分别是左上右下
        mBaseToolbar.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String ObjectId = intent.getStringExtra("ObjectId");

        query(ObjectId);

        mBaseToolbar.setBackgroundColor(Color.TRANSPARENT);
        mBaseToolbar.setTitle("笔记");
        mBaseToolbar.setTitleTextColor(Color.BLACK);
        mBaseToolbar.setBackButton(R.drawable.ic_back);
        mBaseToolbar.addRightText("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateNote(ObjectId);
//                ToastUtils.showShort(ObjectId);

            }
        });

    }

    /**
     * 查询一个对象
     *
     * @param mObjectId
     */
    private void query(String mObjectId) {
        BmobQuery<DataBean> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(mObjectId, new QueryListener<DataBean>() {
            @Override
            public void done(DataBean dataBean, BmobException e) {
                if (e == null) {
                    String note = dataBean.getNote();
                    if (note==null) {
                        mTvNote.setText("暂无笔记");
                    } else {
                        mTvNote.setText(note);
                    }
//                    Snackbar.make(mBtnQuery, "查询成功：" + category.getName(), Snackbar.LENGTH_LONG).show();
                } else {
                    Log.e("BMOB", e.toString());
//                    Snackbar.make(mBtnQuery, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 更新一个对象
     */
    private void updateNote(String mObjectId) {
        String note = mEtNote.getText().toString();

        DataBean dataBean = new DataBean();
        dataBean.setNote(note);
        dataBean.update(mObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShort("更新成功");
                    mTvNote.setText(note);
//                    Snackbar.make(mBtnUpdate, "更新成功", Snackbar.LENGTH_LONG).show();
                } else {
                    Log.e("BMOB", e.toString());
                    ToastUtils.showShort("更新成功");

//                    Snackbar.make(mBtnUpdate, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void widgetClick(View view) {

    }
}
