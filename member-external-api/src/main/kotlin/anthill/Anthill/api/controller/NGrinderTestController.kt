package anthill.Anthill.api.controller

import org.springframework.web.bind.annotation.RestController

@RestController
class NGrinderTestController {
    fun helloNGrinder(): String{
        return "hello"
    }
}