package cn.leizy.retrofitdemo.retrofit.ann

/**
 * @author Created by wulei
 * @date 2021/1/5, 005
 * @description
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Query(val value:String)
