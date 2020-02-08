package org.forsbootpractice.practicesboot.api;

import org.springframework.web.bind.annotation.*;

@RestController
public class FirstController {

//    @GetMapping("")
//    public String hello() {
//        return "hello GoodGood!";
//    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String hello2() {
        return "Nice to meet you!";
    }

    @GetMapping("/name/{name}")
    public String getName(@PathVariable(value = "name") final String name) {
        return name;
    }

    @GetMapping("/part")
    public String getpart(@RequestParam(value = "part",defaultValue = "") final String part,@RequestParam(value = "type",defaultValue = "") final String type) {
        return part+"에서"+type + "을 맡고있습니다";
    }


}
