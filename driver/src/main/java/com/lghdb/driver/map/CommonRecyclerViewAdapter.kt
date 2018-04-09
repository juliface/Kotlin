package com.lghdb.driver.map

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lghdb.driver.extensions.ctx

/**
 * Created by lghdb on 2018/4/8.
 */
class CommonRecyclerViewAdapter<T>(private val tips:MutableList<T>,
                                   private val layoutId:Int,
                                   private val bindFun:(View,T)->Unit,
                           private val itemClick:(T)->Unit) :
        RecyclerView.Adapter<CommonRecyclerViewAdapter.ViewHolder<T>>(){

    /**
     * 创建item的方法
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T>? {
        val view = LayoutInflater.from(parent.ctx).inflate(layoutId, parent, false)
        return ViewHolder(view,bindFun,itemClick)
    }

    /**
     * 渲染数据的方法
     */
    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bindData(tips[position])
    }

    override fun getItemCount(): Int = tips?.size?: 0

    class ViewHolder<T>(view: View,
                        private val bindFun:(View,T)->Unit,
                        private val itemClick:(T)->Unit) : RecyclerView.ViewHolder(view){

        fun bindData(t: T){
            with(t){
                bindFun(itemView, t)
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }


}