package fit.wenchao.simplecodebase

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimpleCodebaseApplication

fun main(args: Array<String>) {
    runApplication<SimpleCodebaseApplication>(*args)
}
