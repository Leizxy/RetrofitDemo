package cn.leizy.retrofitdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.leizy.retrofitdemo.retrofit.Api
import cn.leizy.retrofitdemo.retrofit.MyRetrofit
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val retrofit = MyRetrofit.Builder().baseUrl("https://restapi.amap.com").build()
        api = retrofit.create(Api::class.java)


    }

    fun http(view: View) {
        Thread {
            val call: Call = api.getWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b")
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val string = response.body?.string()
                    Log.i("MainActivity", "onResponse: $string")
                }
            })
        }.start()
    }
}