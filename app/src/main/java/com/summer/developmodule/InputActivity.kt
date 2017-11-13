package com.summer.developmodule

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.activity_input.*
import org.jetbrains.anko.find

/**
 * Created by KXT on 2017/11/10.
 */
class InputActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
//        val contentFrameLayout = find<ViewGroup>(Window.ID_ANDROID_CONTENT)
//        val parentView = contentFrameLayout.getChildAt(0)
//        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
//            parentView.fitsSystemWindows = false
//        }

//        mytoolbar.setToolBar("我的收藏", true, "",-1, true, "", -1)

    }


}