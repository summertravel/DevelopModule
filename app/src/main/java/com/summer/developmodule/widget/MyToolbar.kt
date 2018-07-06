package com.summer.developmodule.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toolbar
import com.summer.developmodule.R
import com.summer.developmodule.R.id.toolbar_title
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.app.Activity
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.ConvertUtils
import com.summer.developmodule.util.ResourceUtil


/**
 * Created by KXT on 2017/11/13.
 */
class MyToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : Toolbar(context, attrs, defStyleAttr) {
    private var view: View? = null
    private var toolbarTitle: TextView? = null
    private var amRightTv: TextView? = null
    private var amRightTv2: TextView? = null
    private var amLeftTv: TextView? = null
    //布局
    private var mInflater: LayoutInflater? = null
    val isNull = -1


    init {
        //初始化函数
        initView()
//        setContentInsetsRelative(10, 10)
        //设置默认销毁当前页面
        if (amLeftTv != null)
            amLeftTv!!.setOnClickListener { (getContext() as Activity).finish() }
    }

    private fun initView() {
        if (view == null) {
            //初始化
            mInflater = LayoutInflater.from(context)
            //添加布局文件
            view = mInflater!!.inflate(R.layout.toolbar, null)
            //绑定控件
            toolbarTitle = view!!.findViewById(R.id.toolbar_title)
            amRightTv = view!!.findViewById(R.id.am_right_tv)
            amRightTv2 = view!!.findViewById(R.id.am_right_tv2)
            amLeftTv = view!!.findViewById(R.id.am_left_tv)
            val layoutParams = Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL)
            addView(view, layoutParams)
        }
    }

    //判断是否有头部
    fun initToolBar() {
        title = ""
        isEnabled = true
    }

    fun setToolBar(title: Int, isshowL: Boolean, wzidL: Int, pidL: Int, isshowR: Boolean, wzidR: Int, pidR: Int) {
        initToolBar()
        setTitleText(title)
        if (isshowL) {
            if (wzidL != isNull) {
                setLeftText(wzidL)
            }
            if (pidL != isNull) {
                setLeftImg(pidL)
            }

        }
        if (isshowR) {
            if (wzidR != isNull) {
                setRightText(wzidR)
            }
            if (pidR != -1) {
                setRightImg(pidR)
            }
        }
    }

    fun setToolBar(title: String, isShowL: Boolean, wzidL: String, pidL: Int, isShowR: Boolean, wzidR: String, pidR: Int) {
        initToolBar()
        setTitleText(title)
        if (isShowL) {
            if (!TextUtils.isEmpty(wzidL)) {
                setLeftText(wzidL)
            }
            if (pidL != isNull) {
                setLeftImg(pidL)
            }
        } else {
            setLeftButtonOnClickLinster(null)
        }
        if (isShowR) {
            if (!TextUtils.isEmpty(wzidR)) {
                setRightText(wzidR)
            }
            if (pidR != isNull) {
                setRightImg(pidR)
            }
        }
    }

    //设置右侧按钮监听事件
    fun setRightButtonOnClickLinster(linster: View.OnClickListener) {
            amRightTv?.setOnClickListener(linster)
    }

    //设置左侧按钮监听事件
    fun setLeftButtonOnClickLinster(linster: View.OnClickListener?) {
            amLeftTv?.setOnClickListener(linster)
    }


    fun setTitleText(text: String) {
            toolbarTitle?.setText(text)
    }

    override fun setTitleTextColor(@ColorRes color: Int) {
            toolbarTitle?.setTextColor(ResourceUtil.getColor(context, color))
    }

    fun setTitleText(@StringRes textId: Int) {
            toolbarTitle?.setText(textId)
    }

    fun setTitleBg(@ColorRes color: Int) {
    }

    fun setTitleBgCopy(@DrawableRes drawable: Int) {
    }

    //设置左边图片
    fun setLeftImg(@DrawableRes imgId: Int) {
            amLeftTv?.setCompoundDrawablesWithIntrinsicBounds(imgId, 0, 0, 0)
    }

    fun setLeftImg2(@DrawableRes imgId: Int) {
            amLeftTv?.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgId, 0)
        amLeftTv?.setCompoundDrawablePadding(ConvertUtils.dp2px(5F))
    }

    //设置左边文字
    fun setLeftText(text: String) {
            amLeftTv?.setText(text)
    }

    //设置左边字体颜色
    fun setLeftTextColor(@ColorRes color: Int) {
            amLeftTv?.setTextColor(ResourceUtil.getColor(context, color))
    }

    fun setLeftText(@StringRes textId: Int) {
            amLeftTv?.setText(textId)
    }

    //设置右边文字
    fun setRightText(text: String) {
            amRightTv?.setText(text)
    }

    //设置右边字体颜色
    fun setRightTextColor(@ColorRes color: Int) {
            amRightTv?.setTextColor(ResourceUtil.getColor(context, color))
    }

    fun setRightText(@StringRes textId: Int) {
            amRightTv?.setText(textId)
    }

    //设置右边图片
    fun setRightImg(@DrawableRes imgId: Int) {
            amRightTv?.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgId, 0)
    }

    @SuppressLint("ResourceType")
    fun setTitleData(title: String, @ColorRes color: Int) {
        setTitleText(title)
        setTitleTextColor(color)
    }


    //=========
    //设置右边图片
    fun setRightImg2(@DrawableRes imgId: Int) {
            amRightTv2?.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgId, 0)
    }

    fun setRightText2(@StringRes textId: Int) {
            amRightTv2?.setText(textId)
    }

    //设置右边文字
    fun setRightText2(text: String) {
            amRightTv2?.setText(text)
    }

    //设置右边字体颜色
    fun setRightTextColor2(@ColorRes color: Int) {
            amRightTv2?.setTextColor(ResourceUtil.getColor(context, color))
    }

    //设置右侧按钮监听事件
    fun setRightButton2OnClickLinster(linster: View.OnClickListener) {
            amRightTv2?.setOnClickListener(linster)
    }

    fun setRTVisible() {
        if (amRightTv2?.getVisibility() != View.VISIBLE) {
            amRightTv2?.setVisibility(View.VISIBLE)
        }
    }
}
