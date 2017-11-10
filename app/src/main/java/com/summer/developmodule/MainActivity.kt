package com.summer.developmodule

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.os.Build
import android.view.View


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolBar.setOnClickListener{startActivity(Intent(MainActivity@this,InputActivity::class.java))}
//        var mainactivity = this
//        var contentFramenLayout  =mainactivity.find<ViewGroup>(Window.ID_ANDROID_CONTENT)
//        contentFramenLayout.getChildAt(0)?.fitsSystemWindows = false
//        setSupportActionBar(toolBar)
//        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

    }


}

