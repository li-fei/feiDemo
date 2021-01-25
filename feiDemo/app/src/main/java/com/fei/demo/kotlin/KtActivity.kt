package com.fei.demo.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fei.demo.R
import com.fei.demo.utils.Logg

class KtActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kt)
    }


    open class Base {
        fun baseFun1() {
            Logg.loge("baseFun1")
        }
    }

    interface MyInter {
        fun inter1()
    }

    //    在Kotlin里，继承关系统一用“:”，不需要向java那样区分implement和extend，在继承多个类/接口时，中间用“,”区分即可，另外，在继承类时，类后面要跟()
    class My constructor() : Base(), MyInter {

        companion object {
            val TAG = "My"
            fun newInstance(): My {
                var m = My()
//                var m2 = My("",1)
                return m
            }
        }

        var name: String = ""
        var age: Int = 0

        init {

        }

        constructor(name: String) : this() {
            this.name = name
        }

        constructor(name: String, age: Int) : this() {
            this.name = name
            this.age = age
        }

        override fun inter1() {
            TODO("Not yet implemented")
        }


    }

}