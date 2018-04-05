package com.lghdb.driver.extensions

import android.support.design.widget.TabLayout

/**
 * Created by lghdb on 2018/4/5.
 */
fun TabLayout.onTabSelected(tabSelect: (TabLayout.Tab?)->Unit){
    this.addOnTabSelectedListener(object: OnTabSelectedListener(){
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tabSelect(tab)
        }
    })
}
fun TabLayout.onTabUnselected(tabUnselected: (TabLayout.Tab?)->Unit){
    this.addOnTabSelectedListener(object: OnTabSelectedListener(){
        override fun onTabUnselected(tab: TabLayout.Tab?) {
            tabUnselected(tab)
        }
    })
}
fun TabLayout.onTabReselected(tabReselected: (TabLayout.Tab?)->Unit){
    this.addOnTabSelectedListener(object: OnTabSelectedListener(){
        override fun onTabReselected(tab: TabLayout.Tab?) {
            tabReselected(tab)
        }
    })
}

open class OnTabSelectedListener: TabLayout.OnTabSelectedListener{
    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {

    }
}

