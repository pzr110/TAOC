package com.pzr.taoc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.pzr.taoc.adapter.CatalogAdapter;
import com.pzr.taoc.adapter.SearchAdapter;
import com.pzr.taoc.bean.CatalogBean;
import com.pzr.taoc.bean.DataBean;
import com.pzr.taoc.ui.catalog.CatalogActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static com.pzr.taoc.MainActivity.getStatusBarHeight;
import static com.pzr.taoc.MyApplication.getContext;

public class SearchActivity extends BaseActivity {
    private BaseToolbar mBaseToolbar;
    private RecyclerView mRecyclerView;
    private EditText mEtSearch;
    private ImageView mIvSearch;


    private List<DataBean> mDataBeans;
    private SearchAdapter mAdapter;

    private String searchStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        BarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        mBaseToolbar = findViewById(R.id.baseToolbar);
        mRecyclerView = findViewById(R.id.recyclerView);
        mEtSearch = findViewById(R.id.et_search);
        mIvSearch = findViewById(R.id.iv_search);
        mIvSearch.setOnClickListener(this);

        int spanCount = 2;
        int spacing = 40;
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);//一列
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mDataBeans = new ArrayList<>();
        mAdapter = new SearchAdapter(mDataBeans);
        mRecyclerView.setAdapter(mAdapter);


        int actionBarHeight = getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, actionBarHeight, 0, 0);//4个参数按顺序分别是左上右下
        mBaseToolbar.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {
        mBaseToolbar.setBackgroundColor(Color.TRANSPARENT);
        mBaseToolbar.setTitle("搜索");
        mBaseToolbar.setTitleTextColor(Color.BLACK);
        mBaseToolbar.setBackButton(R.drawable.ic_back);

//        getInfo();
//        save();
        initListenerNew();
    }

    private void initListenerNew() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                DataBean dataBean = mAdapter.getData().get(i);
//                String id = dataBean.getId();
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
//                intent.putExtra("catalogId", id);
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
        BmobQuery<DataBean> bmobQuery = new BmobQuery<DataBean>();
        bmobQuery.findObjects(new FindListener<DataBean>() {
            @Override
            public void done(List<DataBean> list, BmobException e) {
                if (e == null) {
                    searchStr = mEtSearch.getText().toString();
                    List<DataBean> beanList = new ArrayList<>();
                    if (searchStr != null&& !searchStr.isEmpty()) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getOriginal().contains(searchStr) || list.get(i).getTranslate().contains(searchStr)) {
                                beanList.add(list.get(i));
                            }else{
                                ToastUtils.showShort("暂无数据");
                            }
                        }
                    } else {
                        ToastUtils.showShort("请输入关键字");
                    }


//                    if (searchStr==null)
//                    ToastUtils.showShort("成功");
                    mAdapter.setNewData(beanList);
                } else {
                    Log.e("PZRZRZ", "ERR：" + e.toString());
                    ToastUtils.showShort(e.toString());
                }
            }
        });
    }

    @Override
    public void widgetClick(View view) {
        if (view.getId() == R.id.iv_search) {
            getInfo();
        }
    }
}
