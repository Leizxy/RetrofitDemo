package cn.leizy.retrofitdemo.retrofit

/**
 * @author Created by wulei
 * @date 2021/1/5, 005
 * @description
 */
abstract class ParameterHandler {
    abstract fun apply(serviceMethod: ServiceMethod, value: String)

    class QueryParameterHandler(val key: String) : ParameterHandler() {
        override fun apply(serviceMethod: ServiceMethod, value: String) {
            serviceMethod.addQueryParameter(key, value)
        }
    }

    class FiledParameterHandler(val key: String) : ParameterHandler() {
        override fun apply(serviceMethod: ServiceMethod, value: String) {
            serviceMethod.addFiledParameter(key, value)
        }
    }
}