package com.fxymine4ever.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fxymine4ever.imageloader.R
import com.fxymine4ever.main.imageloader.ImageLoader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loader = ImageLoader.getInstance(this)
        Thread{
            loader.bindBitmap("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2193446071,2937423247&fm=173&app=49&f=JPEG?w=640&h=960&s=30B31A9AC3514BDC073B3FD10300D0BE",
                    iv)
        }.start()
    }
}
