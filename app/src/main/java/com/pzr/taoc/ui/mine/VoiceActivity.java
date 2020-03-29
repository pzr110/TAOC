package com.pzr.taoc.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.pzr.taoc.BaseActivity;
import com.pzr.taoc.NoteActivity;
import com.pzr.taoc.R;
import com.pzr.taoc.adapter.NoteListAdapter;
import com.pzr.taoc.adapter.VoiceBean;
import com.pzr.taoc.adapter.VoiceListAdapter;
import com.pzr.taoc.bean.DataBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.pzr.taoc.MainActivity.getStatusBarHeight;
import static com.pzr.taoc.MyApplication.getContext;

public class VoiceActivity extends BaseActivity {
    private BaseToolbar mBaseToolbar;
    private RecyclerView mRecyclerView;

    private List<VoiceBean> mVoiceBeans;
    private VoiceListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        BarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_voice;
    }

    @Override
    protected void initView() {
        mBaseToolbar = findViewById(R.id.baseToolbar);
        mRecyclerView = findViewById(R.id.recyclerView);

        int spanCount = 2;
        int spacing = 40;
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);//一列
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mVoiceBeans = new ArrayList<>();
        mAdapter = new VoiceListAdapter(mVoiceBeans);
        mRecyclerView.setAdapter(mAdapter);

        int actionBarHeight = getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, actionBarHeight, 0, 0);//4个参数按顺序分别是左上右下
        mBaseToolbar.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {
        mBaseToolbar.setBackgroundColor(Color.TRANSPARENT);
        mBaseToolbar.setTitle("我的笔记");
        mBaseToolbar.setTitleTextColor(Color.BLACK);
        mBaseToolbar.setBackButton(R.drawable.ic_back);

        getInfo();
//        save();
        initListenerNew();
    }

    private void initListenerNew() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                DataBean dataBean = mAdapter.getData().get(i);
//                String objectId = dataBean.getObjectId();
//                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
//                intent.putExtra("ObjectId", objectId);
//                startActivity(intent);
////                finish();

            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                VoiceBean voiceBean = mAdapter.getData().get(i);
                String voiceFile = voiceBean.getVoiceFile();
//                String objectId = voiceBean.getObjectId();
                File file = new File(voiceFile);
                if (view.getId() == R.id.iv_delete) {
                    mAdapter.remove(i);
                    FileUtils.delete(voiceFile);
                }
            }
        });

    }

    private void update(String oId) {
        DataBean dataBean = new DataBean();
        dataBean.setNote("");
        dataBean.update(oId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShort("删除成功");
//                    Snackbar.make(mBtnUpdate, "更新成功", Snackbar.LENGTH_LONG).show();
                } else {
                    Log.e("BMOB", e.toString());
                    ToastUtils.showShort("删除失败");
//                    Snackbar.make(mBtnUpdate, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getInfo() {
        String path = Environment.getExternalStorageDirectory().getPath();
        List<File> files = FileUtils.listFilesInDir(path);

        Log.e("FILEPZR", "P:" + files);
//        ToastUtils.showShort("" + files);

        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).toString().contains(".mp3")) {
                Log.e("FILEPZR", "MP3:" + files.get(i));

                VoiceBean voiceBean = new VoiceBean("0", "音频位置：", files.get(i).toString());
                List<VoiceBean> list = new ArrayList<>();
                list.add(voiceBean);
                mAdapter.setNewData(list);
            }
        }


    }

    @Override
    public void widgetClick(View view) {

    }
}
