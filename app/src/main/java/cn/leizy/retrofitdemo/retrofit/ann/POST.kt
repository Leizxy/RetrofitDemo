package cn.leizy.retrofitdemo.retrofit.ann


/**
 * @author Created by wulei
 * @date 2021/1/5, 005
 * @description
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class POST(val value: String = "")
