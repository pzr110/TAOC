package com.pzr.taoc.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pzr.taoc.R;
import com.pzr.taoc.bean.DataBean;

import java.util.List;

public class VoiceListAdapter extends BaseQuickAdapter<VoiceBean, BaseViewHolder> {

    public VoiceListAdapter(@Nullable List<VoiceBean> data) {
        super(R.layout.item_note_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VoiceBean item) {
        if (item.getVoiceName() == null) {
            helper.setText(R.id.tv_note_title, "为空");
        } else {
            helper.setText(R.id.tv_note_title, item.getVoiceName());
        }
        if (item.getVoiceFile() == null) {
            helper.setText(R.id.tv_note_content, "暂无");
        } else {
            helper.setText(R.id.tv_note_content, item.getVoiceFile());
        }

        helper.addOnClickListener(R.id.iv_delete);


    }
}
