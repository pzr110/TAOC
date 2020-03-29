package com.pzr.taoc.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pzr.taoc.R;
import com.pzr.taoc.bean.CatalogBean;
import com.pzr.taoc.bean.DataBean;

import java.util.List;

public class SearchAdapter extends BaseQuickAdapter<DataBean, BaseViewHolder> {

    public SearchAdapter(@Nullable List<DataBean> data) {
        super(R.layout.item_catalog, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataBean item) {
        helper.setText(R.id.tv_title, item.getOriginal())
                .setText(R.id.tv_content, item.getTranslate());
    }
}
