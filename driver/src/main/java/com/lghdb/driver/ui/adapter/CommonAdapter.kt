package com.lghdb.driver.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup


/**
 * Created by lghdb on 2018/4/3.
 */

class CommonAdapter() :
        RecyclerView.Adapter<CommonAdapter.ViewHolder>(){

    /**
     * 创建item的方法
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        return null
    }

    /**
     * 渲染数据的方法
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 0

    class ViewHolder(view: View
                     )
        : RecyclerView.ViewHolder(view){



    }

}