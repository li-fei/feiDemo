package com.fei.demo.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fei.demo.R
import com.fei.demo.utils.Logg

class KotlinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
    }

    override fun onDestroy() {
        Logg.loge("KotlinActivity onDestroy");
        super.onDestroy()
    }
}