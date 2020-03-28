package com.pzr.taoc;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.pzr.taoc.bean.DataBean;
import com.pzr.taoc.utils.comment.CommentDialogMutiAdapter;
import com.pzr.taoc.utils.comment.InputTextMsgDialog;
import com.pzr.taoc.utils.comment.bean.CommentEntity;
import com.pzr.taoc.utils.comment.bean.CommentMoreBean;
import com.pzr.taoc.utils.comment.bean.FirstLevelBean;
import com.pzr.taoc.utils.comment.bean.SecondLevelBean;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    private BaseToolbar mBaseToolbar;
    private Switch mSwithTranslate;
    private TextView mTvTranslate;

    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private AssetManager mAssetManager;
    private ImageView mIvPlay;

    private Spinner mSpinnerSpeed;
    private double mSpeed;

    private ImageView mTvDownload;

    private TextView mTvOriginal;
    private FloatingActionButton mFabComment;
    private FloatingActionButton mFabNote;


    private ImageView mIvLeft;
    private ImageView mIvRight;

    int mId = 0;
    private int mSize;

    private CommentDialogMutiAdapter bottomSheetAdapter;
    private float slideOffset = 0;
    private BottomSheetDialog bottomSheetDialog;
    private String content = "我听见你的声音，有种特别的感觉。让我不断想，不敢再忘记你。如果真的有一天，爱情理想会实现，我会加倍努力好好对你，永远不改变";

    private List<MultiItemEntity> data = new ArrayList<>();
    private List<FirstLevelBean> datas = new ArrayList<>();
    private RecyclerView rv_dialog_lists;

    private InputTextMsgDialog inputTextMsgDialog;
    private int offsetY;

    private long totalCount = 22;

    final RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity or Fragment instance
    private String mObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        BarUtils.setStatusBarLightMode(this, true);
        Aria.download(this).register();

        loadData(mId);

        initCommentData();
        dataSort(0);
        showSheetDialog();

        mAssetManager = getResources().getAssets();
        //权限判断，如果没有权限就请求权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initMediaPlayer(mId);//初始化播放器 MediaPlayer
        }

    }

    //原始数据 一般是从服务器接口请求过来的
    private void initCommentData() {
        int size = 10;
        for (int i = 0; i < size; i++) {
            FirstLevelBean firstLevelBean = new FirstLevelBean();
            firstLevelBean.setContent("第" + (i + 1) + "人评论内容" + (i % 3 == 0 ? content + (i + 1) + "次" : ""));
            firstLevelBean.setCreateTime(System.currentTimeMillis());
            firstLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3370302115,85956606&fm=26&gp=0.jpg");
            firstLevelBean.setId(i + "");
            firstLevelBean.setIsLike(0);
            firstLevelBean.setLikeCount(i);
            firstLevelBean.setUserName("星梦缘" + (i + 1));
            firstLevelBean.setTotalCount(i + size);

            List<SecondLevelBean> beans = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                SecondLevelBean secondLevelBean = new SecondLevelBean();
                secondLevelBean.setContent("一级第" + (i + 1) + "人 二级第" + (j + 1) + "人评论内容" + (j % 3 == 0 ? content + (j + 1) + "次" : ""));
                secondLevelBean.setCreateTime(System.currentTimeMillis());
                secondLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
                secondLevelBean.setId(j + "");
                secondLevelBean.setIsLike(0);
                secondLevelBean.setLikeCount(j);
                secondLevelBean.setUserName("星梦缘" + (i + 1) + "  " + (j + 1));
                secondLevelBean.setIsReply(j % 5 == 0 ? 1 : 0);
                secondLevelBean.setReplyUserName(j % 5 == 0 ? "闭嘴家族" + j : "");
                secondLevelBean.setTotalCount(firstLevelBean.getTotalCount());
                beans.add(secondLevelBean);
                firstLevelBean.setSecondLevelBeans(beans);
            }
            datas.add(firstLevelBean);
        }
    }

    /**
     * 对数据重新进行排列
     * 目的是为了让一级评论和二级评论同为item
     * 解决滑动卡顿问题
     *
     * @param position
     */
    private void dataSort(int position) {
        if (datas.isEmpty()) {
            data.add(new MultiItemEntity() {
                @Override
                public int getItemType() {
                    return CommentEntity.TYPE_COMMENT_EMPTY;
                }
            });
            return;
        }

        if (position <= 0) data.clear();
        int posCount = data.size();
        int count = datas.size();
        for (int i = 0; i < count; i++) {
            if (i < position) continue;

            //一级评论
            FirstLevelBean firstLevelBean = datas.get(i);
            if (firstLevelBean == null) continue;
            firstLevelBean.setPosition(i);
            posCount += 2;
            List<SecondLevelBean> secondLevelBeans = firstLevelBean.getSecondLevelBeans();
            if (secondLevelBeans == null || secondLevelBeans.isEmpty()) {
                firstLevelBean.setPositionCount(posCount);
                data.add(firstLevelBean);
                continue;
            }
            int beanSize = secondLevelBeans.size();
            posCount += beanSize;
            firstLevelBean.setPositionCount(posCount);
            data.add(firstLevelBean);

            //二级评论
            for (int j = 0; j < beanSize; j++) {
                SecondLevelBean secondLevelBean = secondLevelBeans.get(j);
                secondLevelBean.setChildPosition(j);
                secondLevelBean.setPosition(i);
                secondLevelBean.setPositionCount(posCount);
                data.add(secondLevelBean);
            }

            //展示更多的item
            if (beanSize <= 18) {
                CommentMoreBean moreBean = new CommentMoreBean();
                moreBean.setPosition(i);
                moreBean.setPositionCount(posCount);
                moreBean.setTotalCount(firstLevelBean.getTotalCount());
                data.add(moreBean);
            }

        }
    }

    private void showSheetDialog() {
        if (bottomSheetDialog != null) {
            return;
        }
        View view = View.inflate(this, R.layout.dialog_bottomsheet, null);
        ImageView iv_dialog_close = (ImageView) view.findViewById(R.id.dialog_bottomsheet_iv_close);
        rv_dialog_lists = (RecyclerView) view.findViewById(R.id.dialog_bottomsheet_rv_lists);
        RelativeLayout rl_comment = view.findViewById(R.id.rl_comment);

        iv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        rl_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加二级评论
                initInputTextMsgDialog(null, false, null, -1);
            }
        });

        bottomSheetAdapter = new CommentDialogMutiAdapter(data);
        rv_dialog_lists.setHasFixedSize(true);
        rv_dialog_lists.setLayoutManager(new LinearLayoutManager(this));
        closeDefaultAnimator(rv_dialog_lists);
        bottomSheetAdapter.setOnLoadMoreListener(this, rv_dialog_lists);
        rv_dialog_lists.setAdapter(bottomSheetAdapter);

        bottomSheetAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view1, int position) {
                switch ((int) view1.getTag()) {
                    case CommentEntity.TYPE_COMMENT_PARENT:
                        if (view1.getId() == R.id.rl_group) {
                            //添加二级评论
                            MainActivity.this.initInputTextMsgDialog((View) view1.getParent(), false, bottomSheetAdapter.getData().get(position), position);
                        } else if (view1.getId() == R.id.ll_like) {
                            //一级评论点赞 项目中还得通知服务器 成功才可以修改
                            FirstLevelBean bean = (FirstLevelBean) bottomSheetAdapter.getData().get(position);
                            bean.setLikeCount(bean.getLikeCount() + (bean.getIsLike() == 0 ? 1 : -1));
                            bean.setIsLike(bean.getIsLike() == 0 ? 1 : 0);
                            datas.set(bean.getPosition(), bean);
                            MainActivity.this.dataSort(0);
                            bottomSheetAdapter.notifyDataSetChanged();
                        }
                        break;
                    case CommentEntity.TYPE_COMMENT_CHILD:

                        if (view1.getId() == R.id.rl_group) {
                            //添加二级评论（回复）
                            MainActivity.this.initInputTextMsgDialog(view1, true, bottomSheetAdapter.getData().get(position), position);
                        } else if (view1.getId() == R.id.ll_like) {
                            //二级评论点赞 项目中还得通知服务器 成功才可以修改
                            SecondLevelBean bean = (SecondLevelBean) bottomSheetAdapter.getData().get(position);
                            bean.setLikeCount(bean.getLikeCount() + (bean.getIsLike() == 0 ? 1 : -1));
                            bean.setIsLike(bean.getIsLike() == 0 ? 1 : 0);

                            List<SecondLevelBean> secondLevelBeans = datas.get((int) bean.getPosition()).getSecondLevelBeans();
                            secondLevelBeans.set(bean.getChildPosition(), bean);
//                            CommentMultiActivity.this.dataSort(0);
                            bottomSheetAdapter.notifyDataSetChanged();
                        }

                        break;
                    case CommentEntity.TYPE_COMMENT_MORE:
                        //在项目中是从服务器获取数据，其实就是二级评论分页获取
                        CommentMoreBean moreBean = (CommentMoreBean) bottomSheetAdapter.getData().get(position);
                        SecondLevelBean secondLevelBean = new SecondLevelBean();
                        secondLevelBean.setContent("more comment" + 1);
                        secondLevelBean.setCreateTime(System.currentTimeMillis());
                        secondLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
                        secondLevelBean.setId(1 + "");
                        secondLevelBean.setIsLike(0);
                        secondLevelBean.setLikeCount(0);
                        secondLevelBean.setUserName("星梦缘" + 1);
                        secondLevelBean.setIsReply(0);
                        secondLevelBean.setReplyUserName("闭嘴家族" + 1);
                        secondLevelBean.setTotalCount(moreBean.getTotalCount() + 1);

                        datas.get((int) moreBean.getPosition()).getSecondLevelBeans().add(secondLevelBean);
                        MainActivity.this.dataSort(0);
                        bottomSheetAdapter.notifyDataSetChanged();

                        break;
                    case CommentEntity.TYPE_COMMENT_EMPTY:
                        MainActivity.this.initRefresh();
                        break;

                }

            }
        });

        bottomSheetDialog = new BottomSheetDialog(this, R.style.dialog);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        final BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        mDialogBehavior.setPeekHeight(getWindowHeight());

        //dialog滑动监听
        mDialogBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    if (slideOffset <= -0.28) {
                        //当向下滑动时 值为负数
                        bottomSheetDialog.dismiss();
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                MainActivity.this.slideOffset = slideOffset;//记录滑动值
            }
        });
    }

    private int getWindowHeight() {
        Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return displayMetrics.heightPixels;
    }


    private void initRefresh() {
        datas.clear();
        initData();
        dataSort(0);
        bottomSheetAdapter.setNewData(data);
    }

    /**
     * 关闭默认局部刷新动画
     */
    public void closeDefaultAnimator(RecyclerView mRvCustomer) {
        if (null == mRvCustomer) return;
        mRvCustomer.getItemAnimator().setAddDuration(0);
        mRvCustomer.getItemAnimator().setChangeDuration(0);
        mRvCustomer.getItemAnimator().setMoveDuration(0);
        mRvCustomer.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) mRvCustomer.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    int positionCount = 0;

    private void initInputTextMsgDialog(View view, final boolean isReply, final MultiItemEntity item, final int position) {
        dismissInputDialog();
        if (view != null) {
            offsetY = view.getTop();
            scrollLocation(offsetY);
        }
        if (inputTextMsgDialog == null) {
            inputTextMsgDialog = new InputTextMsgDialog(this, R.style.dialog_center);
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    addComment(isReply, item, position, msg);
                }

                @Override
                public void dismiss() {
                    //item滑动到原位
                    scrollLocation(-offsetY);
                }
            });
        }
        showInputTextMsgDialog();
    }

    private void showInputTextMsgDialog() {
        inputTextMsgDialog.show();
    }

    //添加评论
    private void addComment(boolean isReply, MultiItemEntity item, final int position, String msg) {
        final String userName = "hui";
        if (position >= 0) {
            //添加二级评论
            int pos = 0;
            String replyUserName = "未知";
            if (item instanceof FirstLevelBean) {
                FirstLevelBean firstLevelBean = (FirstLevelBean) item;
                positionCount = (int) (firstLevelBean.getPositionCount() + 1);
                pos = (int) firstLevelBean.getPosition();
                replyUserName = firstLevelBean.getUserName();
            } else if (item instanceof SecondLevelBean) {
                SecondLevelBean secondLevelBean = (SecondLevelBean) item;
                positionCount = (int) (secondLevelBean.getPositionCount() + 1);
                pos = (int) secondLevelBean.getPosition();
                replyUserName = secondLevelBean.getUserName();
            }

            SecondLevelBean secondLevelBean = new SecondLevelBean();
            secondLevelBean.setReplyUserName(replyUserName);
            secondLevelBean.setIsReply(isReply ? 1 : 0);
            secondLevelBean.setContent(msg);
            secondLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3370302115,85956606&fm=26&gp=0.jpg");
            secondLevelBean.setCreateTime(System.currentTimeMillis());
            secondLevelBean.setIsLike(0);
            secondLevelBean.setUserName(userName);
            secondLevelBean.setId("");
            secondLevelBean.setPosition(positionCount);

            datas.get(pos).getSecondLevelBeans().add(secondLevelBean);
            MainActivity.this.dataSort(0);
            bottomSheetAdapter.notifyDataSetChanged();
            rv_dialog_lists.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((LinearLayoutManager) rv_dialog_lists.getLayoutManager())
                            .scrollToPositionWithOffset(positionCount >= data.size() - 1 ? data.size() - 1
                                    : positionCount, positionCount >= data.size() - 1 ? Integer.MIN_VALUE : rv_dialog_lists.getHeight());
                }
            }, 100);

        } else {
            //添加一级评论
            FirstLevelBean firstLevelBean = new FirstLevelBean();
            firstLevelBean.setUserName(userName);
            firstLevelBean.setId(bottomSheetAdapter.getItemCount() + 1 + "");
            firstLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
            firstLevelBean.setCreateTime(System.currentTimeMillis());
            firstLevelBean.setContent(msg);
            firstLevelBean.setLikeCount(0);
            firstLevelBean.setSecondLevelBeans(new ArrayList<SecondLevelBean>());
            datas.add(0, firstLevelBean);
            MainActivity.this.dataSort(0);
            bottomSheetAdapter.notifyDataSetChanged();
            rv_dialog_lists.scrollToPosition(0);
        }
    }

    // item滑动
    public void scrollLocation(int offsetY) {
        try {
            rv_dialog_lists.smoothScrollBy(0, offsetY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissInputDialog() {
        if (inputTextMsgDialog != null) {
            if (inputTextMsgDialog.isShowing()) inputTextMsgDialog.dismiss();
            inputTextMsgDialog.cancel();
            inputTextMsgDialog = null;
        }
    }


    private void setPlayerSpeed(float speed) {
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.M) {
            PlaybackParams playbackParams = mMediaPlayer.getPlaybackParams();
            playbackParams.setSpeed(speed);
            mMediaPlayer.setPlaybackParams(playbackParams);
        }
    }

    private void initMediaPlayer(int id) {
        try {
//            File file = new File(Environment.getExternalStorageDirectory(), "music.mp3");
            AssetFileDescriptor fileDescriptor = mAssetManager.openFd("audio_" + id + ".mp3");
//            AssetFileDescriptor fileDescriptor = mAssetManager.openFd( "1.mp3");
//            ToastUtils.showShort("audio_" + id + ".mp3");
//            Uri uri = Uri.parse("https://ljytly.oss-cn-chengdu.aliyuncs.com/voice/audio_"+id+".mp3");
            mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getStartOffset());//指定音频文件路径
//            mMediaPlayer.setLooping(true);//设置为循环播放
//            mMediaPlayer.setDataSource(this,uri);
            mMediaPlayer.prepare();//初始化播放器MediaPlayer

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMediaPlayer(mId);
                } else {
                    Toast.makeText(this, "拒绝权限，将无法使用程序。", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mBaseToolbar = findViewById(R.id.baseToolbar);
        mSwithTranslate = findViewById(R.id.swith_translate);
        mSwithTranslate.setChecked(true);
        mTvTranslate = findViewById(R.id.tv_translate);
        mIvPlay = findViewById(R.id.iv_play);
        mIvPlay.setOnClickListener(this);
        mTvDownload = findViewById(R.id.tv_download);
        mTvDownload.setOnClickListener(this);
        mSpinnerSpeed = findViewById(R.id.spinner_speed);

        mTvOriginal = findViewById(R.id.tv_original);
        mFabComment = findViewById(R.id.fab_comment);
        mFabNote = findViewById(R.id.fab_note);
        mIvLeft = findViewById(R.id.iv_left);
        mIvRight = findViewById(R.id.iv_right);
        mIvLeft.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        mFabComment.setOnClickListener(this);
        mFabNote.setOnClickListener(this);

        mSpinnerSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
//                    case 0:
                    case 2: {
                        mSpeed = 1;
                        setPlayerSpeed(1);
                        Play();
                        break;
                    }
                    case 1: {
                        setPlayerSpeed((float) 0.5);
                        Play();
                        break;
                    }
                    case 3: {
                        mSpeed = 1.5;
                        setPlayerSpeed((float) 1.5);
                        Play();
                        break;
                    }
                    case 4: {
                        mSpeed = 2;
                        setPlayerSpeed((float) 2);
                        Play();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        initViewInfo();

        // git test

    }

    private void Play() {
        if (mMediaPlayer.isPlaying()) {
//            mMediaPlayer.start();
            mIvPlay.setBackgroundResource(R.drawable.ic_pause);
        } else {
//            mMediaPlayer.pause();
            mIvPlay.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private void initViewInfo() {
        int actionBarHeight = getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, actionBarHeight, 0, 0);//4个参数按顺序分别是左上右下
        mBaseToolbar.setLayoutParams(layoutParams);

        mSwithTranslate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ToastUtils.showShort("开启");
                    mTvTranslate.setVisibility(View.VISIBLE);
                } else {
                    ToastUtils.showShort("关闭");
                    mTvTranslate.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    @Override
    protected void initData() {
        // ToolBar
        mBaseToolbar.setBackgroundColor(Color.TRANSPARENT);
        mBaseToolbar.setTitle("学而第一");
        mBaseToolbar.setTitleTextColor(Color.BLACK);
        mBaseToolbar.addLeftImage(R.drawable.ic_catalog, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("目录");
            }
        });

        mBaseToolbar.addRightImage(R.drawable.ic_user, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(MineActivity.class);
//                ToastUtils.showShort("个人中心");
            }
        });


    }

    /**
     * name为football的类别
     */
    private void equal() {
        BmobQuery<DataBean> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("id", 1);
        categoryBmobQuery.findObjects(new FindListener<DataBean>() {
            @Override
            public void done(List<DataBean> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort("查询成功" + object.get(0).getVoice().toString());
//                    Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                } else {
                    Log.e("BMOB", e.toString());
                    ToastUtils.showShort("查询失败");
//                    Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void widgetClick(View view) {
        switch (view.getId()) {

            case R.id.tv_download: {
                ToastUtils.showShort("下载中...");
                MyPermissions();
                downStart();

                break;

            }

            case R.id.iv_play: {
                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                    mIvPlay.setBackgroundResource(R.drawable.ic_pause);
                } else {
                    mMediaPlayer.pause();
                    mIvPlay.setBackgroundResource(R.drawable.ic_play);
                }
                break;

            }

            case R.id.iv_left: {
                mId -= 1;
                loadData(mId);
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                initMediaPlayer(mId);

                mIvPlay.setBackgroundResource(R.drawable.ic_play);

                if (mId >= 0 && mId < mSize) {
                    mIvRight.setEnabled(true);
                    mIvLeft.setEnabled(true);
                }
                break;
            }
            case R.id.iv_right: {
                mId += 1;
                loadData(mId);
                initMediaPlayer(mId);
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                initMediaPlayer(mId);
                mIvPlay.setBackgroundResource(R.drawable.ic_play);

                if (mId >= 0 && mId < mSize) {
                    mIvRight.setEnabled(true);
                    mIvLeft.setEnabled(true);
                }
                break;
            }
            case R.id.fab_comment: {
                ToastUtils.showShort("展开评论");
                bottomSheetAdapter.notifyDataSetChanged();
                slideOffset = 0;
                bottomSheetDialog.show();
                break;
            }
            case R.id.fab_note: {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra("ObjectId", mObjectId);
                ActivityUtils.startActivity(intent);
                break;
            }
        }
    }

    @SuppressLint("CheckResult")
    private void MyPermissions() {
        //申请运行时权限
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                .subscribe(granted -> {
                    if (granted) {
                        ToastUtils.showShort("允许权限");
                        downStart();
                    } else {
                        ToastUtils.showShort("禁止权限");
                    }
                });

    }

    private void downStart() {
        ToastUtils.showShort("开始下载");
        Uri uri = Uri.parse("https://ljytly.oss-cn-chengdu.aliyuncs.com/voice/audio_" + mId + ".mp3");
        String downloadCachePath = Environment.getExternalStorageDirectory().getPath() + "/" + mId + ".mp3";
        Aria.download(this)
                .load(uri.toString())     //读取下载地址
                .setFilePath(downloadCachePath) //设置文件保存的完整路径
                .create();//创建并启动下载


    }

    @Download.onPre
    void onPre(DownloadTask task) {
//        mAdapter.updateState(task.getEntity());
        Log.e(TAG, "onPre");
    }

    @Download.onTaskRunning
    void running(DownloadTask task) {
//        if(task.getKey().eques(url)){
//		....
//            可以通过url判断是否是指定任务的回调
//        }
        int p = task.getPercent();    //任务进度百分比
        String speed = task.getConvertSpeed();    //转换单位后的下载速度，单位转换需要在配置文件中打开
        long speed1 = task.getSpeed(); //原始byte长度速度
        Log.e(TAG, "running:" + task);
    }

    @Download.onTaskStart
    void taskStart(DownloadTask task) {
//        Log.d(TAG, task.getTaskName() + ", " + task.getState());
//        mAdapter.updateState(task.getEntity());
        Log.e(TAG, "taskStart");

    }

    @Download.onTaskResume
    void taskResume(DownloadTask task) {
//        Log.d(TAG, task.getTaskName() + ", " + task.getState());
//        mAdapter.updateState(task.getEntity());
        Log.e(TAG, "taskResume");

    }

    @Download.onTaskStop
    void taskStop(DownloadTask task) {
//        mAdapter.updateState(task.getEntity());
        Log.e(TAG, "taskStop");

    }

    @Download.onTaskCancel
    void taskCancel(DownloadTask task) {
//        mAdapter.updateState(task.getEntity());
        Log.e(TAG, "taskCancel");

    }

    @Download.onTaskFail
    void taskFail(DownloadTask task) {
//        mAdapter.updateState(task.getEntity());
        Log.e(TAG, "taskFail" + task);

    }

    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
//        mAdapter.updateState(task.getEntity());
        Log.e(TAG, "taskComplete");
        ToastUtils.showShort("下载完成");

    }

    @Download.onTaskRunning()
    void taskRunning(DownloadTask task) {
//        mAdapter.setProgress(task.getEntity());
        Log.e(TAG, "taskRunning");

    }


    private void loadData(int id) {
        BmobQuery<DataBean> bmobQuery = new BmobQuery<DataBean>();
        bmobQuery.addQueryKeys("objectId,original,translate");
        bmobQuery.findObjects(new FindListener<DataBean>() {
            @Override
            public void done(List<DataBean> list, BmobException e) {
                if (e == null) {
                    mSize = list.size();
                    if (id >= 0 && id < mSize) {
                        DataBean dataBean = list.get(id);
                        mObjectId = dataBean.getObjectId();
//                        ToastUtils.showShort(mObjectId);
                        mTvOriginal.setText(dataBean.getOriginal());
                        mTvTranslate.setText(dataBean.getTranslate());
                    } else if (id < 0) {
//                        mIvRight.setEnabled(false);
                        mIvLeft.setEnabled(false);
                        mId = 0;
                        ToastUtils.showShort("没有了...");
                    } else if (id >= mSize) {
                        mIvRight.setEnabled(false);
                        mId = mSize - 1;
                        ToastUtils.showShort("没有了...");
                    }

                } else {
                    ToastUtils.showShort(e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        if (datas.size() >= totalCount) {
            bottomSheetAdapter.loadMoreEnd(false);
            return;
        }
        FirstLevelBean firstLevelBean = new FirstLevelBean();
        firstLevelBean.setUserName("hui");
        firstLevelBean.setId((datas.size() + 1) + "");
        firstLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
        firstLevelBean.setCreateTime(System.currentTimeMillis());
        firstLevelBean.setContent("add loadmore comment");
        firstLevelBean.setLikeCount(0);
        firstLevelBean.setSecondLevelBeans(new ArrayList<SecondLevelBean>());
        datas.add(firstLevelBean);
        dataSort(datas.size() - 1);
        bottomSheetAdapter.notifyDataSetChanged();
        bottomSheetAdapter.loadMoreComplete();

    }
}
