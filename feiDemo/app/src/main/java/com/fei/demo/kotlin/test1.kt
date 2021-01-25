package com.fei.demo.kotlin

import com.fei.demo.utils.Logg

/*
    Object:取消，在Java里Object是所有类的基类，但在Kotlin里，基类改成了Any
    Any:新增，Kotlin里所有类的基类
    object:新增，Kotlin是区分大小写的，object是Kotlin中的单例类
    new:取消，Kotlin不需要new关键字
    abstract:仍然表示抽象类
    interface:仍然表示接口
    static:取消！Java用static去共享同一块内存空间，这是一个非常实用的设计，不过Kotlin移除了static，用伴随对象（前面提到过的compaion object）的概念替换了static，
    伴随对象其实是个单例的实体，所以伴随对象比static更加灵活一些，能去继承和扩展。
*/

//    final:取消，Kotlin的继承和Java不同，Java的类默认可继承，只有final修饰的类不能继承；
//    Kotlin的类默认不能继承，只有为open修饰的类能继承
open class Base {
    fun baseFun1() {
        Logg.loge("baseFun1")
    }
}

interface MyInter {
    fun inter1()
}

//    在Kotlin里，继承关系统一用“:”，不需要向java那样区分implement和extend，在继承多个类/接口时，中间用“,”区分即可，另外，在继承类时，类后面要跟()
class My constructor(name: String) : Base(), MyInter {

    companion object {
        val TAG = "My"
        fun newInstance(): My {
            var m = My("lifei")
            return m
        }
    }

    var name: String = name
    var age: Int = 0

    init {

    }

    constructor(name: String, age: Int) : this(name) {
        this.age = age
    }

    override fun inter1() {
        TODO("Not yet implemented")
    }

}

fun testMain() {
    var m = My("lifei")
    var m2 = My("yuneec", 1)
    m.baseFun1()
    m.name
    My.TAG
}






class My2 constructor() : KtActivity.Base(), KtActivity.MyInter {

    companion object {
        val TAG = "My"
        fun newInstance(): My2 {
            var m2 = My2()
//                var m2 = My("",1)
            return m2
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

