package com.pzr.taoc.ui.catalog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.pzr.taoc.BaseActivity;
import com.pzr.taoc.MainActivity;
import com.pzr.taoc.R;
import com.pzr.taoc.adapter.CatalogAdapter;
import com.pzr.taoc.bean.CatalogBean;
import com.pzr.taoc.bean.DataBean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static com.pzr.taoc.MainActivity.getStatusBarHeight;
import static com.pzr.taoc.MyApplication.getContext;

public class CatalogActivity extends BaseActivity {

    private BaseToolbar mBaseToolbar;
    private RecyclerView mRecyclerView;

    private List<CatalogBean> mCatalogBeans;
    private CatalogAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        BarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_catalog;
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

        mCatalogBeans = new ArrayList<>();
        mAdapter = new CatalogAdapter(mCatalogBeans);
        mRecyclerView.setAdapter(mAdapter);


        int actionBarHeight = getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, actionBarHeight, 0, 0);//4个参数按顺序分别是左上右下
        mBaseToolbar.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {
        mBaseToolbar.setBackgroundColor(Color.TRANSPARENT);
        mBaseToolbar.setTitle("目录");
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
                CatalogBean catalogBean = mAdapter.getData().get(i);
                String id = catalogBean.getId();
                Intent intent = new Intent(CatalogActivity.this, MainActivity.class);
                intent.putExtra("catalogId", id);
                startActivity(intent);
                finish();

            }
        });
    }

    /**
     * 新增一个对象
     */
    private void save() {
        CatalogBean catalogBean = new CatalogBean();
        catalogBean.setId("1");
        catalogBean.setTitle("学而第一");
        catalogBean.setContent("子曰：学而时习之");
        catalogBean.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
//                    mObjectId = objectId;
//                    Snackbar.make(mBtnSave, "新增成功：" + mObjectId, Snackbar.LENGTH_LONG).show();
                } else {
//                    Log.e("BMOB", e.toString());
//                    Snackbar.make(mBtnSave, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getInfo() {
        BmobQuery<CatalogBean> bmobQuery = new BmobQuery<CatalogBean>();
        bmobQuery.findObjects(new FindListener<CatalogBean>() {
            @Override
            public void done(List<CatalogBean> list, BmobException e) {
                if (e == null) {
//                    ToastUtils.showShort("成功");
                    mAdapter.setNewData(list);
                } else {
                    Log.e("PZRZRZ", "ERR：" + e.toString());
                    ToastUtils.showShort(e.toString());
                }
            }
        });
    }

    @Override
    public void widgetClick(View view) {

    }
}
