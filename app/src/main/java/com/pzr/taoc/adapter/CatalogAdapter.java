package com.pzr.taoc.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pzr.taoc.R;
import com.pzr.taoc.bean.CatalogBean;

import java.util.List;

public class CatalogAdapter extends BaseQuickAdapter<CatalogBean, BaseViewHolder> {

    public CatalogAdapter(@Nullable List<CatalogBean> data) {
        super(R.layout.item_catalog, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CatalogBean item) {
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_content, item.getContent());
    }
}
