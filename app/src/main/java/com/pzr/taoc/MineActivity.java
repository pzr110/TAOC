package com.pzr.taoc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.pzr.taoc.bean.User;
import com.pzr.taoc.ui.mine.NickActivity;
import com.pzr.taoc.ui.mine.SetPassActivity;
import com.pzr.taoc.utils.GlideEngine;
import com.pzr.taoc.utils.NiceImageView;
import com.pzr.taoc.utils.ShareBitmapUtils;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.pzr.taoc.MainActivity.getStatusBarHeight;

public class MineActivity extends BaseActivity {
    private BaseToolbar mBaseToolbar;
    private NiceImageView mOivMineHead;
    private TextView mTvMineNickName;
    private LinearLayout mLlSetPass;
    private LinearLayout mLlMyMsg;
    private LinearLayout mLlMyMp3;
    private LinearLayout mLlMyNote;
    private LinearLayout mLlCancel;

    private AlertDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        BarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_mine;
    }

    @Override
    protected void initView() {
        mBaseToolbar = findViewById(R.id.baseToolbar);
        mOivMineHead = findViewById(R.id.oiv_mine_head);
        mOivMineHead.setOnClickListener(this);
        Drawable headImg = ShareBitmapUtils.getDrawable(this, "headImg", null);
        if (headImg == null) {
            mOivMineHead.setBackgroundResource(R.drawable.ic_default_head);
        } else {
            mOivMineHead.setImageDrawable(headImg);
        }
        mTvMineNickName = findViewById(R.id.tv_mine_nick_name);
        mTvMineNickName.setOnClickListener(this);
        mLlSetPass = findViewById(R.id.ll_set_pass);
        mLlSetPass.setOnClickListener(this);
        mLlMyMsg = findViewById(R.id.ll_my_msg);
        mLlMyMp3 = findViewById(R.id.ll_my_mp3);
        mLlMyNote = findViewById(R.id.ll_my_note);
        mLlCancel = findViewById(R.id.ll_cancel);


        initViewInfo();
    }

    private void initViewInfo() {
        int actionBarHeight = getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, actionBarHeight, 0, 0);//4个参数按顺序分别是左上右下
        mBaseToolbar.setLayoutParams(layoutParams);

    }


    @Override
    protected void initData() {
        mBaseToolbar.setBackgroundColor(Color.TRANSPARENT);
        mBaseToolbar.setTitle("我的");
        mBaseToolbar.setTitleTextColor(Color.BLACK);
        mBaseToolbar.setBackButton(R.drawable.ic_back);

        if (BmobUser.isLogin()) {

            User user = BmobUser.getCurrentUser(User.class);
            String nick = user.getNick();
            mTvMineNickName.setText(nick);

        } else {
            ToastUtils.showShort("尚未登录，请先登录");
//            Snackbar.make(view, "尚未登录，请先登录", Snackbar.LENGTH_LONG).show();
        }

    }


    @Override
    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.oiv_mine_head: {
                if (BmobUser.isLogin()) {
                    HeadImageDialog();
                } else {
                    ToastUtils.showShort("尚未登录，请先登录");
                }
                break;
            }
            case R.id.tv_mine_nick_name: {
//                ActivityUtils.startActivity(NickActivity.class);
                Intent intent = new Intent(MineActivity.this, NickActivity.class);
                MineActivity.this.startActivityForResult(intent, 66);
                break;
            }
            case R.id.ll_set_pass: {
                ActivityUtils.startActivity(SetPassActivity.class);
                break;
            }
        }
    }


    /**
     * 修改头像
     */
    private void HeadImageDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_head_image, null, false);
        dialog = new AlertDialog.Builder(this).setView(view).create();

        Window window = dialog.getWindow();

        //这一句消除白块
        assert window != null;
        window.setBackgroundDrawable(new BitmapDrawable());
        // 打开相册
        TextView tv_dialog_photo_album = view.findViewById(R.id.tv_dialog_photo_album);
        tv_dialog_photo_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(MineActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)
                        .enableCrop(true)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                        .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                        .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .forResult(PictureConfig.CHOOSE_REQUEST);

                dialog.dismiss();
            }
        });
        // 拍照
        TextView tv_dialog_photograph = view.findViewById(R.id.tv_dialog_photograph);
        tv_dialog_photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(MineActivity.this)
                        .openCamera(PictureMimeType.ofImage())
                        .enableCrop(true)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                        .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                        .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                dialog.dismiss();
            }
        });

        TextView tv_dialog_close = view.findViewById(R.id.tv_dialog_close);
        tv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout((ScreenUtils.getScreenWidth() / 4 * 3), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片、视频、音频选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回五种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                // 5.media.getAndroidQToPath();为Android Q版本特有返回的字段，此字段有值就用来做上传使用
                // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩

                // 从2.3.6开始加入了原图功能，所以再使用的时候需要判断media.isOriginal()); 如果为true有可能是用户选择要上传原图则要取  media.getOriginalPath());作为上传路径，前提是你开启了.isOriginalImageControl(true);开关

                for (LocalMedia media : selectList) {
                    Log.i(TAG, "压缩::" + media.getCompressPath());
                    Log.i(TAG, "原图::" + media.getPath());
                    Log.i(TAG, "裁剪::" + media.getCutPath());
                    Log.i(TAG, "是否开启原图::" + media.isOriginal());
                    Log.i(TAG, "原图路径::" + media.getOriginalPath());
                    Log.i(TAG, "Android Q 特有Path::" + media.getAndroidQToPath());
                    File file = new File(media.getCutPath());
//                    uploadHeadImgFile(file);
                    Log.e("Pzr", "File" + file);
//                    ToastUtils.showShort(file + "File");

//                    Uri uri = Uri.parse(mHeadImgUri);
//                    Glide.with(this).load(uri).into(mNivChangeHead);

                    setHeadImg(file);

                }
//                    adapter.setList(selectList);
//                    adapter.notifyDataSetChanged();
            }
        }
//
        if (requestCode == 66 && resultCode == 66) {

            String newNick = data.getStringExtra("newNick");
            mTvMineNickName.setText(newNick);
        }
    }

    private void setHeadImg(File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        Drawable drawable = new BitmapDrawable(bitmap);

//        SPUtils.getInstance().put("headImg",drawable);
        ShareBitmapUtils.putDrawable(this, "headImg", drawable);


//        Drawable headImg = ShareBitmapUtils.getDrawable(this, "headImg", null);

        mOivMineHead.setImageDrawable(drawable);
    }


}
