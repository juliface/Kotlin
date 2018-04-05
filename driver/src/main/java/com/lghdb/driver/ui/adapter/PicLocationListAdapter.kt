package com.lghdb.driver.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.services.help.Tip
import com.lghdb.driver.R
import com.lghdb.driver.data.ViewHoldier
import com.lghdb.driver.extensions.ctx
import kotlinx.android.synthetic.main.item_listview_location.view.*
import kotlinx.android.synthetic.main.item_poi_search.view.*

/**
 * Created by lghdb on 2018/4/3.
 */
class PicLocationListAdapter(private val holdiers:MutableList<ViewHoldier>,
                           private val itemClick:(ViewHoldier)->Unit) :
        RecyclerView.Adapter<PicLocationListAdapter.ViewHolder>(){

    /**
     * 创建item的方法
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_listview_location, parent, false)
        return ViewHolder(view,itemClick)
    }

    /**
     * 渲染数据的方法
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.convert(holdiers[position])
    }

    override fun getItemCount(): Int = holdiers?.size?: 0

    class ViewHolder(view: View,
                     private val itemClick:(ViewHoldier)->Unit) : RecyclerView.ViewHolder(view){

        fun convert(vh: ViewHoldier){
            with(vh){
                itemView.rl_tv_name.text = title
                itemView.rl_tv_location.text = address
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }


}