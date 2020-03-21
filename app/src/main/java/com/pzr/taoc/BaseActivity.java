package com.pzr.taoc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private int lastClickViewId = 0;
    private long lastClickTime = 0;

    //获取TAG的activity名称
    protected final String TAG = this.getClass().getSimpleName();
    //是否显示标题栏
//    private boolean isShowTitle = true;
//    是否显示状态栏
//    private boolean isShowStatusBar = true;
//    是否允许旋转屏幕
//    private boolean isAllowScreenRoate = true;
    //封装Toast对象
    private static Toast toast;
    public Context context;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //activity管理
        ActivityCollector.addActivity(this);
//        if (!isShowTitle) {
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//        }
//
//        if (isShowStatusBar) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
//                    , WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }

        //设置布局
        setContentView(initLayout());
        //设置屏幕是否可旋转
//        if (!isAllowScreenRoate) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
        //初始化控件
        initView();
        //设置数据
        initData();
    }

    /**
     * 初始化布局
     *
     * @return 布局id
     */
    protected abstract int initLayout();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 设置数据
     */
    protected abstract void initData();

    /**
     * 设置是否显示标题栏
     *
     * @param showTitle true or false
//     */
//    public void setShowTitle(boolean showTitle) {
//        isShowTitle = showTitle;
//    }

    /**
     * 设置是否显示状态栏
     *
     * @param showStatusBar true or false
//     */
//    public void setShowStatusBar(boolean showStatusBar) {
//        isShowStatusBar = showStatusBar;
//    }

    /**
     * 是否允许屏幕旋转
     *
     * @param allowScreenRoate true or false
//     */
//    public void setAllowScreenRoate(boolean allowScreenRoate) {
//        isAllowScreenRoate = allowScreenRoate;
//    }

    /**
     * 保证同一按钮在1秒内只会响应一次点击事件
//     */
//    public abstract class OnSingleClickListener implements View.OnClickListener {
//        //两次点击按钮之间的间隔，目前为1000ms
//        private static final int MIN_CLICK_DELAY_TIME = 1000;
//        private long lastClickTime;
//
//        public abstract void onSingleClick(View view);
//
//        @Override
//        public void onClick(View view) {
//            long curClickTime = System.currentTimeMillis();
//            if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
//                lastClickTime = curClickTime;
//                onSingleClick(view);
//            }
//        }
//    }



    /**
     * 同一按钮在短时间内可重复响应点击事件
     */
    public abstract class OnMultiClickListener implements View.OnClickListener {
        public abstract void onMultiClick(View view);

        @Override
        public void onClick(View v) {
            onMultiClick(v);
        }
    }

    /**
     * 显示提示  toast
     *
     * @param msg 提示信息
     */
    @SuppressLint("ShowToast")
    public void showToast(String msg) {
        try {
            if (null == toast) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            } else {
                toast.setText(msg);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }


    //  隐藏键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 防止快速点击
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (notFastClick(v)) {
            widgetClick(v);
        }
    }

    private boolean notFastClick(View view) {
        if (view.getId() == lastClickViewId) {
            if (System.currentTimeMillis() - lastClickTime <= 1000) {
                Log.d("notFastClick", lastClickViewId + "1s内被多次点击");
                return false;
            }
        }
        lastClickViewId = view.getId();
        lastClickTime = System.currentTimeMillis();
        return true;
    }

    public abstract void widgetClick(View view);


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity管理
        ActivityCollector.removeActivity(this);
    }
}
