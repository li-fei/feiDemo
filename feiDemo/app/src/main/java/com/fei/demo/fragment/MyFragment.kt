package com.fei.demo.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fei.demo.R
import com.fei.demo.utils.Logg
import kotlinx.android.synthetic.main.my_fragment.*

class News(var title:String, var content:String ,var isSelected:Boolean)

class MyFragment : Fragment() {

    var list  = ArrayList<News>()

    companion object {
        fun newInstance() = MyFragment()
    }

    private lateinit var viewModel: MyViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.my_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDate()
    }

    private fun initDate(){
        for (num in 1 ..30)
            list.add(News("新闻$num", "震惊$num",false))
        Logg.loge("MyFragment initDate")
        list[1].isSelected = true
        replaceFragment(RightFragment(list[1]))

        recycler.layoutManager = LinearLayoutManager(this.context)
        val adapter = NewsAdapter(this.requireActivity(),list)
        recycler.adapter =  adapter
        adapter.setOnItemClick(object : NewsAdapter.OnItemClickListener {
            override fun OnItemClick(view: View, position: Int) {
                when(position){
                    0->{
                        replaceFragment(MyFragment())
                    }
                    else->{
                        replaceFragment(RightFragment(list[position]))
                    }
                }

                val mMessage = Message.obtain()
                mMessage.what = 1
                val mBundle = Bundle()
                mBundle.putInt("KEY", position)
                mMessage.data = mBundle
                mHandler.sendMessage(mMessage)
            }
        })

    }
    private  fun replaceFragment( fragment:Fragment){
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.right_layout,fragment)
        transaction.commit()
    }

    val mHandler: Handler = object: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                1-> {
                    val mBundle = msg?.data
                    Logg.loge("position ---> " + mBundle.getInt("KEY"))
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        // TODO: Use the ViewModel
    }

}