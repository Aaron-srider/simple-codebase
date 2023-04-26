package fit.wenchao.simplecodebase.consts

class test(var name: String) {

    var firstProperty = "First property: $name".also(::println)

    init {
        println(name)
    }


    constructor(name: String, age: String) : this(name) {
        println(age)
    }

    init {
        println(name)
    }

    var secondProperty = "Second property: ${name.length}".also(::println)

}

class test1(var name: String) {
    init {
        println(name)
    }


    init {
        println(name)
        InitOrderDemo("")
    }
}

class InitOrderDemo(name: String) {
    var firstProperty = "First property: $name".also(::println)



    constructor(name: String, age:String):this("") {

    }

    init {
        println("First initializer block that prints $name")
    }

    var secondProperty = "Second property: ${name.length}".also(::println)

    init {
        println("Second initializer block that prints ${name.length}")
    }
}

fun main() {
    println("hello")
}