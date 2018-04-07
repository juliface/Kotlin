package com.lghdb.driver.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.amap.api.navi.model.AMapNaviPath
import com.lghdb.driver.R
import com.lghdb.driver.extensions.ctx
import android.graphics.Color
import android.os.Handler
import com.lghdb.driver.ui.activities.DriverLineActivity
import kotlinx.android.synthetic.main.item_line_navimap.view.*




/**
 * Created by lghdb on 18-4-5.
 */
class DriverLineAdapter(private val paths:MutableList<AMapNaviPath>,
                        private val driverLine: DriverLineActivity,
                        private val itemClick:(AMapNaviPath,View)->View) :
        RecyclerView.Adapter<DriverLineAdapter.ViewHolder>(){

    /**
     * 创建item的方法
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_recycleview_naviways, parent, false)
        return ViewHolder(view,driverLine,itemClick)
    }

    /**
     * 渲染数据的方法
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.convert(paths[position])
    }

    override fun getItemCount(): Int = paths?.size?: 0

    class ViewHolder(view: View,
                     private val driverLine: DriverLineActivity,
                     private val itemClick:(AMapNaviPath,View)->View) : RecyclerView.ViewHolder(view){

        var maxWdith = 0
        val handler = Handler()

        fun convert(path: AMapNaviPath){
            initItemBackground()
            if (maxWdith == 0){
                handler.post {
                    maxWdith = itemView.ll_tv_labels.width - itemView.ll_tv_labels.paddingLeft - itemView.ll_tv_labels.paddingRight
                }
            }
            var title = path.labels
            if (title.split(",").size >=3){
                title = "推荐"
            }
            handler.post{
                reSizeView(itemView.ll_tv_labels, title)
                reSizeView(itemView.ll_tv_time, driverLine.getTime(path.allTime))
                reSizeView(itemView.ll_tv_length, driverLine.getLength(path.allLength))
            }
            itemView.setOnClickListener {
                selectedBackground()
                val lastView = itemClick(path,itemView)
                if(lastView !== itemView) {
                    cleanSelector(lastView)
                }
            }
        }
        private fun reSizeView(textView:TextView, text:String){
            var paint = textView.paint
            var textWidth = paint.measureText(text)
            var textSizeInDp = textView.textSize
            if (textWidth > maxWdith){
                while (textSizeInDp > 0) {
                    textView.textSize = textSizeInDp
                    paint = textView.paint
                    textWidth = paint.measureText(text)
                    if (textWidth <= maxWdith) {
                        break
                    }
                    textSizeInDp -= 1
                }
            }
            textView.invalidate()
            textView.text = text
        }

        /**
         * 初始化Item样式
         */
        private fun initItemBackground() {
            itemView.ll_itemview.setBackgroundResource(R.drawable.item_naviway_normal_bg)
            itemView.ll_tv_labels.setTextColor(itemView.resources.getColor(R.color.item_text_title_color))
            itemView.ll_tv_length.setTextColor(itemView.resources.getColor(R.color.item_text_title_color))
            itemView.ll_tv_time.setTextColor(itemView.resources.getColor(R.color.black))
            itemView.ll_tv_labels.setBackgroundResource(R.drawable.item_naviway_title_normal)
        }

        /**
         * 选中的背景色修改
         */
        private fun selectedBackground() {
            itemView.ll_itemview.setBackgroundResource(R.drawable.item_naviway_selected_bg)
            itemView.ll_tv_labels.setTextColor(Color.WHITE)
            itemView.ll_tv_time.setTextColor(itemView.resources.getColor(R.color.blue))
            itemView.ll_tv_length.setTextColor(itemView.resources.getColor(R.color.blue))
            itemView.ll_tv_labels.setBackgroundResource(R.drawable.item_naviway_title_selected)
        }

        /**
         * 清除选中的样式
         */
        private fun cleanSelector(view: View) {
            view.setBackgroundResource(R.drawable.item_naviway_normal_bg)
            view.ll_tv_labels.setTextColor(itemView.resources.getColor(R.color.item_text_title_color))
            view.ll_tv_length.setTextColor(itemView.resources.getColor(R.color.item_text_title_color))
            view.ll_tv_time.setTextColor(itemView.resources.getColor(R.color.black))
            view.ll_tv_labels.setBackgroundResource(R.drawable.item_naviway_title_normal)
        }
    }


}