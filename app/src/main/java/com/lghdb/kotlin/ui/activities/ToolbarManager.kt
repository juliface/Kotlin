package com.lghdb.kotlin.ui.activities

import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.lghdb.kotlin.R
import com.lghdb.kotlin.extensions.ctx
import com.lghdb.kotlin.extensions.slideEnter
import com.lghdb.kotlin.extensions.slideExit
import com.lghdb.kotlin.ui.App
import org.jetbrains.anko.toast

/**
 * Created by lghdb on 2018/3/30.
 * 工具条管理类
 */
interface ToolbarManager{
    val toolbar : Toolbar

    var toolbarTitle: String
        get() = toolbar.title.toString()
        set(value){
            toolbar.title = value
        }

    fun initToolBar() {
        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_settings -> App.instance.toast("设置")
                else -> App.instance.toast("其他操作")
            }
            true
        }
    }

    fun enableHomeAsUp(up: () -> Unit) {
        toolbar.navigationIcon = createUpDrawable()
        toolbar.setNavigationOnClickListener{ up() }
    }
    private fun createUpDrawable() = with(DrawerArrowDrawable(toolbar.ctx)){
        progress = 1f
        this
    }
    fun attachToScroll(recyclerView: RecyclerView){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0) toolbar.slideExit() else toolbar.slideEnter()
            }
        })
    }
}