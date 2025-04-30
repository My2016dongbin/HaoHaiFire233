package com.ruyiruyi.rylibrary.base;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruyiruyi.rylibrary.R;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/8/29 15:25
 */

/**
 * 方法使用: ( 其中onForward()方法为右侧点击监听 )
 * //设置是否显示标题栏
 * showTitleBar(true);
 * //是否显示左侧文字
 * showBackwardView(R.string.web_title, true);
 * //设置标题栏背景色
 * setTitleBgColor(R.color.web_top);
 * //设置状态栏颜色
 * setStatus(R.color.web_top);
 * //添加左侧图标，
 * setLeftIcon(R.drawable.ic_cha);
 * //文字颜色
 * setTitleColor(R.color.c7);
 * //是否显示右侧文字
 * showForwardView(R.string.web_title, true);
 * //设置标题
 * setTitle("如驿如意");
 * //设置右侧图标
 * setRightIcon(R.drawable.ic_check_red);
 */
public abstract class BaseWebActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 页面布局
     */
    private FrameLayout fl_content_layout;

    /**
     * 标题栏跟布局
     */
    private RelativeLayout rl_title_bar_layout;

    /**
     * 标题栏右侧图标
     */
    private ImageView iv_right_icon;

    /**
     * 标题栏左侧图标
     */
    private ImageView iv_title_left_icon;

    /**
     * 标题栏标题
     */
    private TextView tv_title_text;

    /**
     * 标题栏左边返回箭头
     */
    private TextView tv_left_icon;

    /**
     * 标题栏右边选项
     */
    private TextView tv_right_title;

    /**
     * 右侧菜单图片点击事件
     */
    private View.OnClickListener rightIconlistener = null;

    /**
     * 右侧菜单图片点击事件监听
     *
     * @param rightIconlistener
     */
    public void setRightIconlistener(View.OnClickListener rightIconlistener) {
        this.rightIconlistener = rightIconlistener;
    }

    /**
     * 左侧菜单图片点击事件
     */
    private View.OnClickListener leftIconlistener = null;

    /**
     * 左侧菜单图片点击事件监听
     *
     * @param leftIconlistener
     */
    public void setLeftIconlistener(View.OnClickListener leftIconlistener) {
        this.leftIconlistener = leftIconlistener;
    }

    /**
     * 左侧标题监听
     */
    private View.OnClickListener leftTitlelistener = null;

    /**
     * 左侧标题监听事件
     *
     * @param leftTitlelistener
     */
    public void setLeftTitlelistener(View.OnClickListener leftTitlelistener) {
        this.leftTitlelistener = leftTitlelistener;
    }

    /**
     * 右侧标题监听
     */
    private View.OnClickListener rightTitlelistener = null;

    /**
     * 右侧标题监听事件
     *
     * @param rightTitlelistener
     */
    public void setRightTitlelistener(View.OnClickListener rightTitlelistener) {
        this.rightTitlelistener = rightTitlelistener;
    }

    /**
     * 页面适配
     *//*
    public ResolutionAdapter mResolutionAdapter;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
        setStatus(R.color.web_top);
    }

    private void setupViews() {
        super.setContentView(R.layout.activity_base_title);
        tv_title_text = (TextView) findViewById(R.id.tv_title_text);
        fl_content_layout = (FrameLayout) findViewById(R.id.fl_content_layout);
        rl_title_bar_layout = (RelativeLayout) findViewById(R.id.rl_title_bar_layout);
        tv_left_icon = (TextView) findViewById(R.id.tv_left_icon);
        tv_right_title = (TextView) findViewById(R.id.tv_right_title);
        iv_right_icon = (ImageView) findViewById(R.id.iv_right_icon);
        iv_title_left_icon = (ImageView) findViewById(R.id.iv_title_left_icon);

        iv_right_icon.setOnClickListener(this);
        iv_title_left_icon.setOnClickListener(this);
        tv_right_title.setOnClickListener(this);
        tv_left_icon.setOnClickListener(this);

      /*  uiAdapter();*/
    }
/*
    *//**
     * 页面适配
     *//*
    private void uiAdapter() {
        try {
            mResolutionAdapter = ResolutionAdapter.getInstance();
            mResolutionAdapter.setDesignResolution(this, new Resolution(750, 1334, 1.0f, 160, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mResolutionAdapter.setTextSize(tv_left_icon, 18);
        mResolutionAdapter.setTextSize(tv_right_title, 18);
        mResolutionAdapter.setup(iv_title_left_icon, 22, 44, ViewScaleType.AS_HEIGHT_EDGES_SCALE);
        mResolutionAdapter.setup(iv_right_icon, 32, 32, ViewScaleType.AS_TWO_EDGES_SCALE);
    }*/

    /**
     * 透明状态栏,透明导航栏
     * 使用了SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION,表示会让应用的主体内容占用
     * 系统导航栏的空间
     * 然后又调用了setNavigationBarColor()方法将导航栏设置成透明色
     */
    public void setStatus(int colorRes) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        } else {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏字体深色
            window.setStatusBarColor(getResources().getColor(colorRes));
        }
    }

    /**
     * 是否显示返回按钮
     *
     * @param backwardResId 文字
     * @param show          true则显示
     */
    public void showBackwardView(int backwardResId, boolean show) {
        if (tv_left_icon != null) {
            if (show) {
                tv_left_icon.setText(backwardResId);
                tv_left_icon.setVisibility(View.VISIBLE);
            } else {
                tv_left_icon.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 提供是否显示提交按钮
     *
     * @param forwardResId 文字
     * @param show         true则显示
     */
    public void showForwardView(int forwardResId, boolean show) {
        if (tv_right_title != null) {
            if (show) {
                tv_right_title.setVisibility(View.VISIBLE);
                tv_right_title.setText(forwardResId);
            } else {
                tv_right_title.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置左侧图标
     *
     * @param resId 图片资源id
     */
    public void setLeftIcon(int resId) {
        iv_title_left_icon.setImageResource(resId);
    }

    /**
     * 设置右侧图标
     *
     * @param redId 图片资源id
     */
    public void setRightIcon(int redId) {
        iv_right_icon.setVisibility(View.VISIBLE);
        iv_right_icon.setImageResource(redId);
    }

    /**
     * 设置标题栏背景色
     */
    public void setTitleBgColor(int colorRes) {
        rl_title_bar_layout.setBackgroundColor(getResources().getColor(colorRes));
    }

    /**
     * 设置是否显示标题栏
     *
     * @param flag true显示
     */
    public void showTitleBar(boolean flag) {
        if (flag) {
            rl_title_bar_layout.setVisibility(View.VISIBLE);
        } else {
            rl_title_bar_layout.setVisibility(View.GONE);
        }
    }

    /**
     * 返回按钮点击后触发
     *
     * @param backwardView 点击的view
     */
    public void onBackward(View backwardView) {
        finish();
    }

    /**
     * 提交按钮点击后触发
     *
     * @param forwardView 点击的view
     */
    public abstract void onForward(View forwardView);

    //设置标题内容
    @Override
    public void setTitle(int titleId) {
        tv_title_text.setText(titleId);
    }

    //设置标题内容
    @Override
    public void setTitle(CharSequence title) {
        tv_title_text.setText(title);
    }

    //设置标题文字颜色
    @Override
    public void setTitleColor(int textColor) {
        tv_title_text.setTextColor(getResources().getColor(textColor));
        tv_left_icon.setTextColor(getResources().getColor(textColor));
        tv_right_title.setTextColor(getResources().getColor(textColor));

    }

    //取出FrameLayout并调用父类removeAllViews()方法
    @Override
    public void setContentView(int layoutResID) {
        fl_content_layout.removeAllViews();
//        View.inflate(this, layoutResID, fl_content_layout);
        LayoutInflater.from(this).inflate(layoutResID, fl_content_layout);
        onContentChanged();
    }

    @Override
    public void setContentView(View view) {
        fl_content_layout.removeAllViews();
        fl_content_layout.addView(view);
        onContentChanged();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        fl_content_layout.removeAllViews();
        fl_content_layout.addView(view, params);
        onContentChanged();
    }

    @Override
    public void onClick(View v) {//点击事件传递
        int i = v.getId();
        if (i == R.id.tv_left_icon) {
            if (leftTitlelistener != null) {
                leftTitlelistener.onClick(v);
            }
        } else if (i == R.id.tv_right_title) {
            if (rightTitlelistener != null) {
                rightTitlelistener.onClick(v);
            }
        } else if (i == R.id.iv_right_icon) {
            if (rightIconlistener != null) {
                rightIconlistener.onClick(v);//右侧菜单栏点击事件传递
            }

        } else if (i == R.id.iv_title_left_icon) {
            if (leftIconlistener != null) {
                leftIconlistener.onClick(v);
            }

        } else {
        }
    }

}
