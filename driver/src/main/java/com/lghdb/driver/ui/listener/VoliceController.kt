package com.lghdb.driver.ui.listener

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.iflytek.cloud.*
import com.lghdb.driver.ui.App
import com.lghdb.driver.ui.listener.interfaces.DriverNaviListener
import org.jetbrains.anko.toast

/**
 * Created by lghdb on 2018/4/2.
 */
class VoliceController private constructor(var context: Context = App.instance): DriverNaviListener {

    companion object {
        val IFLYTEK_APPID = "5ac1e330"
        val TTS_PLAY = 1
        val CHECK_TTS_PLAY = 2
        val instance by lazy { VoliceController() }
    }
    var mtts: SpeechSynthesizer
    var wordList:MutableList<String> = mutableListOf()
    var isPlaying = false
    var handler:Handler


    init {
        SpeechUtility.createUtility(context.applicationContext,
                "${SpeechConstant.APPID}=${IFLYTEK_APPID}")

        mtts = SpeechSynthesizer.createSynthesizer(context){
            sCode:Int -> when(sCode){
                ErrorCode.SUCCESS -> {}
                else -> context.toast("在线合成语音失败")
            }
        }.apply {
            //设置发音人
            setParameter(SpeechConstant.VOICE_NAME,"xiaoyan")
            //设置语速,值范围：[0, 100],默认值：50
            setParameter(SpeechConstant.SPEED, "55")
            //设置音量
            setParameter(SpeechConstant.VOLUME, "tts_volume")
            //设置语调
            setParameter(SpeechConstant.PITCH, "tts_pitch")
        }

        handler = object : Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when(msg.what){
                    TTS_PLAY -> synchronized(mtts) {
                        if(!isPlaying && wordList.size > 0){
                            isPlaying = true
                            val playtts = wordList.removeAt(0)
                            val self = this
                            mtts.startSpeaking(playtts, object:SynthesizerListener{
                                override fun onCompleted(p0: SpeechError?) {
                                    isPlaying = false
                                    self.obtainMessage(1).sendToTarget()
                                }

                                override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {

                                }

                                override fun onBufferProgress(p0: Int, p1: Int, p2: Int, p3: String?) {
                                    isPlaying = true
                                }

                                override fun onSpeakBegin() {
                                    isPlaying = true
                                }

                                override fun onSpeakPaused() {

                                }

                                override fun onSpeakProgress(p0: Int, p1: Int, p2: Int) {
                                    isPlaying = true
                                }

                                override fun onSpeakResumed() {
                                    isPlaying = true
                                }
                            })
                        }
                    }
                    CHECK_TTS_PLAY -> {
                        if(!isPlaying) obtainMessage(1).sendToTarget()
                    }
                }
            }
        }
    }

    fun stopSpeaking(){
        wordList.clear()
        mtts?.stopSpeaking()
        isPlaying = false
    }
    fun destory(){
        wordList.clear()
        mtts?.destroy()
    }

    override fun onCalculateRouteFailure(p0: Int) {
        wordList?.add("路线规划失败")
    }

    override fun onGetNavigationText(p0: String?) {
        if (p0 != null)wordList?.add(p0)
        handler.obtainMessage(CHECK_TTS_PLAY).sendToTarget()
    }

    override fun onReCalculateRouteForTrafficJam() {
        wordList.add("前方路线拥堵，路线重新规划")
    }

    override fun onReCalculateRouteForYaw() {
        wordList.add("路线重新规划");
    }

}