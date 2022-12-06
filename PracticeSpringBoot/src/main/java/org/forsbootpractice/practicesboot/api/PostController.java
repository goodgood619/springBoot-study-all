package org.forsbootpractice.practicesboot.api;

import org.forsbootpractice.practicesboot.dto.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("post")
public class PostController {

    @PostMapping("")
    public String postUser(@RequestBody final User user) {
        return user.getName();
    }

}
