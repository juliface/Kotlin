package com.lghdb.driver.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.amap.api.services.core.AMapException
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.lghdb.driver.R
import com.lghdb.driver.ui.adapter.PoiSearchListAdapter
import kotlinx.android.synthetic.main.activity_poi_search.*

/**
 * Created by lghdb on 2018/4/3.
 * 结束点的搜索功能
 */
class PoiSearchActivity: AppCompatActivity(){

    companion object {
        val END_SITE = "PoiSearchActivity:end_site"
        val POI_LOCATION_MAP = 10
    }
    val city:String? = null
    var tips:MutableList<Tip> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poi_search)
        actionBar?.hide()

        //自动补全
        input_edittext.addTextChangedListener(CityTextWatcher())
        //自动补全list
        ll_rv_inputlist.layoutManager = LinearLayoutManager(this)
        ll_rv_inputlist.adapter = PoiSearchListAdapter(tips){
            val intent = Intent().putExtra("end_address",it)
            setResult(DriverLineActivity.REQUEST_CODE_POI_SEARCH, intent)
            finish()
        }

        //右上角的地图图标事件
        rl_tv_map_pick.setOnClickListener {
            var intent = Intent(this@PoiSearchActivity,PicLocationActivity::class.java)
            startActivityForResult(intent, POI_LOCATION_MAP)
        }

        //左上角的返回按钮
        back_line.setOnClickListener{ cancle(); finish() }
    }

    /**
     * 根据地图上选择的地址回填信息
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (POI_LOCATION_MAP == resultCode){
            val tip = data.getParcelableExtra<Tip>("end_address")
            val intent = Intent().putExtra("end_address",tip)
            setResult(DriverLineActivity.REQUEST_CODE_POI_SEARCH, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        cancle()
        super.onBackPressed()
    }

    private fun cancle(){
        val intent = Intent()
        setResult(DriverLineActivity.REQUEST_CODE_POI_SEARCH_EXIT, intent)
    }

    /**
     * 自动补全的监听器实现
     */
    inner class CityTextWatcher: TextWatcher{

        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(content: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val newText = content.toString().trim()
            val inputquery = InputtipsQuery(newText, city).apply { cityLimit=true }
            val inputtips = Inputtips(this@PoiSearchActivity, inputquery)
            inputtips.setInputtipsListener(object: Inputtips.InputtipsListener{
                override fun onGetInputtips(list: MutableList<Tip>, p1: Int) {
                    if( p1 == AMapException.CODE_AMAP_SUCCESS){
                        if(tips.size > 0) tips.clear()
                        for (tip in list){
                            if (tip.point != null) tips.add(tip)
                        }
                        ll_rv_inputlist.adapter.notifyDataSetChanged()
                    }
                }
            })
            inputtips.requestInputtipsAsyn()
        }
    }
}