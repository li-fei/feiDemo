package com.fei.demo.kotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.fei.demo.R
import com.fei.demo.fragment.MyFragment
import com.fei.demo.utils.Logg
import kotlinx.android.synthetic.main.activity_kotlin.*


class KotlinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

        findViewById<Button>(R.id.btn_kotlin1).setOnClickListener(View.OnClickListener {
            Logg.loge("KotlinActivity btn_kotlin1 click ");
            var a = test(2)
            btn_kotlin1.text = "hello kot " + a
        })

        addFragment(MyFragment.newInstance(),R.id.fragment_container)

        testMain()

    }

    fun test(a: Int): Int {
        return a + 1;
    }


    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    fun addFragment(fragment: Fragment, frameId: Int){
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }

    fun replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{replace(frameId, fragment)}
    }


    override fun onDestroy() {
        Logg.loge("KotlinActivity onDestroy");
        super.onDestroy()
    }



}