package cn.leizy.retrofitdemo.retrofit

import android.util.Log
import cn.leizy.retrofitdemo.retrofit.ann.Field
import cn.leizy.retrofitdemo.retrofit.ann.GET
import cn.leizy.retrofitdemo.retrofit.ann.POST
import cn.leizy.retrofitdemo.retrofit.ann.Query
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Request
import java.lang.reflect.Method

/**
 * @author Created by wulei
 * @date 2021/1/5, 005
 * @description
 */
class ServiceMethod(builder: Builder) {
    private var formBuild: FormBody.Builder? = null
    private var parameterHandler: Array<ParameterHandler?>?
    private var hasBody: Boolean
    private var relativeUrl: String
    private var httpMethod: String
    private var baseUrl: HttpUrl
    private var callFactory: Call.Factory
    private var urlBuilder: HttpUrl.Builder? = null

    init {
        this.baseUrl = builder.retrofit.baseUrl
        this.callFactory = builder.retrofit.callFactory
        this.httpMethod = builder.httpMethod
        this.relativeUrl = builder.relativeUrl
        this.hasBody = builder.hasBody
        this.parameterHandler = builder.parameterHandler
        Log.i("ServiceMethod", ": $hasBody")
        if (hasBody) {
            formBuild = FormBody.Builder()
        }
    }

    fun invoke(args: Array<Any>): Call {
        //处理url
        parameterHandler?.forEachIndexed { index, handlers ->
            handlers?.apply(this, args[index].toString())
        }

        if (urlBuilder == null) {
            urlBuilder = baseUrl.newBuilder(relativeUrl)
        }
        var url: HttpUrl = urlBuilder!!.build()

        var formBody: FormBody? = null
        if (formBuild != null) {
            formBody = formBuild!!.build()
        }
        val request: Request = Request.Builder().url(url).method(httpMethod, formBody).build()
        return callFactory.newCall(request)
    }

    fun addQueryParameter(key: String, value: String) {
        if (urlBuilder == null) {
            urlBuilder = baseUrl.newBuilder(relativeUrl)
        }
        urlBuilder!!.addQueryParameter(key, value)
    }

    fun addFiledParameter(key: String, value: String) {
        formBuild!!.add(key, value)
    }

    class Builder(val retrofit: MyRetrofit, method: Method) {
        var hasBody: Boolean = false
        var relativeUrl: String = ""
        var httpMethod: String = "GET"
        var parameterAnnotations: Array<Array<Annotation>> = method.parameterAnnotations
        var annotations: Array<Annotation> = method.annotations
        var parameterHandler: Array<ParameterHandler?>? = null

        fun build(): ServiceMethod {
            //解析方法上的注解
            annotations.forEach {
                if (it is POST) {
                    this.httpMethod = "POST"
                    this.relativeUrl = it.value
                    this.hasBody = true
                } else if (it is GET) {
                    this.httpMethod = "GET"
                    this.relativeUrl = it.value
                    this.hasBody = false
                }
            }

            //解析参数注解
            /**
             * 2 解析方法参数的注解
             */
            val length = parameterAnnotations.size
            parameterHandler = arrayOfNulls(length)
            parameterAnnotations.forEachIndexed { index, it ->
                it.forEach { annotation ->
                    if (annotation is Query) {
                        val value = annotation.value
                        parameterHandler?.run {
                            this[index] = ParameterHandler.QueryParameterHandler(value)
                        }
                    } else if (annotation is Field) {
                        val value = annotation.value
                        parameterHandler?.run {
                            this[index] = ParameterHandler.FiledParameterHandler(value)
                        }
                    }
                }
            }

            return ServiceMethod(this)
        }

    }
}