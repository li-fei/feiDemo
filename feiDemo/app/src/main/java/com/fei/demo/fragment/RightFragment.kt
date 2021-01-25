package com.fei.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fei.demo.R
import kotlinx.android.synthetic.main.right_fragment.view.*

class RightFragment(val news: News) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.right_fragment, container, false)
        view.tv_title.text = news.title
        view.tv_content.text = news.content
        return view
    }

}