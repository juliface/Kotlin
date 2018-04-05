package com.lghdb.driver.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.services.help.Tip
import com.lghdb.driver.R
import com.lghdb.driver.extensions.ctx
import kotlinx.android.synthetic.main.item_poi_search.view.*

/**
 * Created by lghdb on 2018/4/3.
 */
class PoiSearchListAdapter(private val tips:MutableList<Tip>,
                           private val itemClick:(Tip)->Unit) :
        RecyclerView.Adapter<PoiSearchListAdapter.ViewHolder>(){

    /**
     * 创建item的方法
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_poi_search, parent, false)
        return ViewHolder(view,itemClick)
    }

    /**
     * 渲染数据的方法
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.convert(tips[position])
    }

    override fun getItemCount(): Int = tips?.size?: 0

    class ViewHolder(view: View,
                     private val itemClick:(Tip)->Unit) : RecyclerView.ViewHolder(view){

        fun convert(tip: Tip){
            with(tip){
                itemView.poi_field_id.text = name
                itemView.poi_value_id.text = district
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }


    }