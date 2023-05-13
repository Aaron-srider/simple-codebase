package fit.wenchao.simplecodebase.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    var counter = 0L

    @GetMapping("/test")
    fun test(){
        for(i in 1..10) {
            println("${counter++} hello")
        }
    }

}