package com.lghdb.kotlin.domain.command

/**
 * Created by lghdb on 2018/3/27.
 * 执行的命令接口
 */
interface Command<out T>{
    fun execute() : T
}