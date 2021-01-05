package cn.leizy.retrofitdemo.retrofit

import android.util.Log
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.lang.RuntimeException
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * @author Created by wulei
 * @date 2021/1/5, 005
 * @description
 */
class MyRetrofit(val callFactory: Call.Factory, val baseUrl: HttpUrl) {
    private var serviceMethodMap: MutableMap<Method, ServiceMethod> = hashMapOf()

    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader, arrayOf<Class<*>>(service)
        ) { _, method, args ->
            val loadMethod: ServiceMethod = loadMethod(method)
            loadMethod.invoke(args)
        } as T
    }

    private fun loadMethod(method: Method): ServiceMethod {
        var serviceMethod = serviceMethodMap[method]
        if (serviceMethod != null) {
            return serviceMethod
        }
        synchronized(serviceMethodMap) {
            serviceMethod = serviceMethodMap[method]
            if (serviceMethod == null) {
                serviceMethod = ServiceMethod.Builder(this, method).build()
                serviceMethodMap[method] = serviceMethod!!
            }
        }
        return serviceMethod!!
    }

    class Builder {
        private var baseUrl: HttpUrl? = null
        private var callFactory: Call.Factory? = null

        fun baseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl.toHttpUrl()
            return this
        }

        fun build(): MyRetrofit {
            if (baseUrl == null) {
                throw RuntimeException("base url is empty")
            }
            if (callFactory == null) {
                callFactory = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor {
                        Log.w("Demo", it)
                    })
                    .build()
            }
            return MyRetrofit(callFactory!!, baseUrl!!)
        }
    }
}