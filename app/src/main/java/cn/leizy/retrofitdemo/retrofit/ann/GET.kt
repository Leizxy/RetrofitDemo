package cn.leizy.retrofitdemo.retrofit.ann

/**
 * @author Created by wulei
 * @date 2021/1/5, 005
 * @description
 */
@Target(AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class GET(val value:String = "")
