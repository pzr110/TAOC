package com.pzr.taoc.utils.comment.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import cn.bmob.v3.BmobObject;

import static com.pzr.taoc.utils.comment.bean.CommentEntity.TYPE_COMMENT_MORE;


/**
 * @author ganhuanhui
 * 时间：2019/12/12 0012
 * 描述：更多item
 */
public class CommentMoreBean extends BmobObject implements MultiItemEntity {

    private long totalCount;
    private long position;
    private long positionCount;

    @Override
    public int getItemType() {
        return TYPE_COMMENT_MORE;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(long positionCount) {
        this.positionCount = positionCount;
    }
}
