package com.fei.demo.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fei.demo.R
import com.fei.demo.utils.Logg
import kotlinx.android.synthetic.main.my_fragment.*

class News(var title:String, var content:String )

class MyFragment : Fragment() {

    var list  = ArrayList<News>()

    companion object {
        fun newInstance() = MyFragment()
    }

    private lateinit var viewModel: MyViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.my_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDate()
    }

    private fun initDate(){
        for (num in 1 ..30)
            list.add(News("新闻$num", "震惊$num"))
        Logg.loge("MyFragment initDate")

        recycler.layoutManager = LinearLayoutManager(context)
        val adapter = NewsAdapter(list)
        recycler.adapter =  adapter
        adapter.setOnItemClick(object : NewsAdapter.OnItemClickListener {
            override fun OnItemClick(view: View, position: Int) {
                replaceFragment(RightFragment(list[position]))
            }
        })
    }
    private  fun replaceFragment( fragment:Fragment){
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.right_layout,fragment)
        transaction.commit()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        // TODO: Use the ViewModel
    }

}