package com.pzr.taoc.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pzr.taoc.R;
import com.pzr.taoc.bean.CatalogBean;
import com.pzr.taoc.bean.DataBean;

import java.util.List;

public class NoteListAdapter extends BaseQuickAdapter<DataBean, BaseViewHolder> {

    public NoteListAdapter(@Nullable List<DataBean> data) {
        super(R.layout.item_note_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataBean item) {
        if (item.getOriginal() == null) {
            helper.setText(R.id.tv_note_title, "标题为空");
        } else {
            helper.setText(R.id.tv_note_title, item.getOriginal());
        }
        if (item.getNote() == null) {
            helper.setText(R.id.tv_note_content, "暂无笔记");
        } else {
            helper.setText(R.id.tv_note_content, item.getNote());
        }

        helper.addOnClickListener(R.id.iv_delete);


    }
}
