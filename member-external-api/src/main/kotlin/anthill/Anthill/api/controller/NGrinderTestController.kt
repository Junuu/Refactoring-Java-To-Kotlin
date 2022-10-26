package anthill.Anthill.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stress-test")
class NGrinderTestController {

    @GetMapping
    fun helloNGrinder(): String{
        return "hello"
    }
}