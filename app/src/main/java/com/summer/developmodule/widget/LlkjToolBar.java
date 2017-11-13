package com.summer.developmodule.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.summer.developmodule.R;
import com.summer.developmodule.util.ResourceUtil;


/**
 * 自定义Toolbar
 * Created by yujunlong on 2017/1/6.
 */

public class LlkjToolBar extends Toolbar {
    private View view;
    private TextView toolbarTitle;
    private TextView amRightTv;
    private TextView amRightTv2;
    private TextView amLeftTv;
    public static final int isNull = -1;
    //布局
    private LayoutInflater mInflater;

    public LlkjToolBar(Context context) {
        this(context, null);
    }

    public LlkjToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LlkjToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化函数
        initView();
        setContentInsetsRelative(0, 0);
        //设置默认销毁当前页面
        if (amLeftTv != null)
            amLeftTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) getContext()).finish();
                }
            });
    }

    private void initView() {
        if (view == null) {
            //初始化
            mInflater = LayoutInflater.from(getContext());
            //添加布局文件
            view = mInflater.inflate(R.layout.toolbar, null);
            //绑定控件
            toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
            amRightTv = (TextView) view.findViewById(R.id.am_right_tv);
            amRightTv2 = (TextView) view.findViewById(R.id.am_right_tv2);
            amLeftTv = (TextView) view.findViewById(R.id.am_left_tv);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL);
            addView(view, layoutParams);
        }
    }

    //判断是否有头部
    public void initToolBar() {
        setTitle("");
        setEnabled(true);
    }

    public void setToolBar(int title, boolean isshowL, int wzidL, int pidL, boolean isshowR, int wzidR, int pidR) {
        initToolBar();
        setTitleText(title);
        if (isshowL) {
            if (wzidL != isNull) {
                setLeftText(wzidL);
            }
            if (pidL != isNull) {
                setLeftImg(pidL);
            }

        }
        if (isshowR) {
            if (wzidR != isNull) {
                setRightText(wzidR);
            }
            if (pidR != -1) {
                setRightImg(pidR);
            }
        }
    }

    public void setToolBar(String title, boolean isShowL, String wzidL, int pidL, boolean isShowR, String wzidR, int pidR) {
        initToolBar();
        setTitleText(title);
        if (isShowL) {
            if (!TextUtils.isEmpty(wzidL)) {
                setLeftText(wzidL);
            }
            if (pidL != isNull) {
                setLeftImg(pidL);
            }
        } else {
            setLeftButtonOnClickLinster(null);
        }
        if (isShowR) {
            if (!TextUtils.isEmpty(wzidR)) {
                setRightText(wzidR);
            }
            if (pidR != isNull) {
                setRightImg(pidR);
            }
        }
    }

    //设置右侧按钮监听事件
    public void setRightButtonOnClickLinster(OnClickListener linster) {
        if (amRightTv != null)
            amRightTv.setOnClickListener(linster);
    }

    //设置左侧按钮监听事件
    public void setLeftButtonOnClickLinster(OnClickListener linster) {
        if (amLeftTv != null)
            amLeftTv.setOnClickListener(linster);
    }


    public void setTitleText(String text) {
        if (toolbarTitle != null)
            toolbarTitle.setText(text);
    }

    public void setTitleTextColor(@ColorRes int color) {
        if (toolbarTitle != null)
            toolbarTitle.setTextColor(ResourceUtil.getColor(getContext(), color));
    }

    public void setTitleText(@StringRes int textId) {
        if (toolbarTitle != null)
            toolbarTitle.setText(textId);
    }

    public void setTitleBg(@ColorRes int color) {
        if (view != null && view.findViewById(R.id.toolbarbg) != null) {
            view.findViewById(R.id.toolbarbg).setBackgroundColor(ResourceUtil.getColor(getContext(), color));
        }
    }

    public void setTitleBgCopy(@DrawableRes int drawable) {
        if (view != null && view.findViewById(R.id.toolbarbg) != null) {
            view.findViewById(R.id.toolbarbg).setBackgroundResource(drawable);
        }
    }

    //设置左边图片
    public void setLeftImg(@DrawableRes int imgId) {
        if (amLeftTv != null)
            amLeftTv.setCompoundDrawablesWithIntrinsicBounds(imgId, 0, 0, 0);
    }

    public void setLeftImg2(@DrawableRes int imgId) {
        if (amLeftTv != null)
            amLeftTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgId, 0);
        amLeftTv.setCompoundDrawablePadding(ConvertUtils.dp2px(5));
    }

    //设置左边文字
    public void setLeftText(String text) {
        if (amLeftTv != null)
            amLeftTv.setText(text);
    }

    //设置左边字体颜色
    public void setLeftTextColor(@ColorRes int color) {
        if (amLeftTv != null)
            amLeftTv.setTextColor(ResourceUtil.getColor(getContext(), color));
    }

    public void setLeftText(@StringRes int textId) {
        if (amLeftTv != null)
            amLeftTv.setText(textId);
    }

    //设置右边文字
    public void setRightText(String text) {
        if (amRightTv != null)
            amRightTv.setText(text);
    }

    //设置右边字体颜色
    public void setRightTextColor(@ColorRes int color) {
        if (amRightTv != null)
            amRightTv.setTextColor(ResourceUtil.getColor(getContext(), color));
    }

    public void setRightText(@StringRes int textId) {
        if (amRightTv != null)
            amRightTv.setText(textId);
    }

    //设置右边图片
    public void setRightImg(@DrawableRes int imgId) {
        if (amRightTv != null)
            amRightTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgId, 0);
    }

    @SuppressLint("ResourceAsColor")
    public void setTitleData(String title, @ColorRes int color) {
        setTitleText(title);
        setTitleTextColor(color);
    }


    //=========
    //设置右边图片
    public void setRightImg2(@DrawableRes int imgId) {
        if (amRightTv2 != null)
            amRightTv2.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgId, 0);
    }

    public void setRightText2(@StringRes int textId) {
        if (amRightTv2 != null)
            amRightTv2.setText(textId);
    }

    //设置右边文字
    public void setRightText2(String text) {
        if (amRightTv2 != null)
            amRightTv2.setText(text);
    }

    //设置右边字体颜色
    public void setRightTextColor2(@ColorRes int color) {
        if (amRightTv2 != null)
            amRightTv2.setTextColor(ResourceUtil.getColor(getContext(), color));
    }

    //设置右侧按钮监听事件
    public void setRightButton2OnClickLinster(OnClickListener linster) {
        if (amRightTv2 != null)
            amRightTv2.setOnClickListener(linster);
    }

    public void setRTVisible() {
        if (amRightTv2.getVisibility() != View.VISIBLE) {
            amRightTv2.setVisibility(View.VISIBLE);
        }
    }
}
