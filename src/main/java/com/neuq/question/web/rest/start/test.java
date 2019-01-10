package com.neuq.question.web.rest.start;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangshyi
 * @date 2019/1/3  14:11
 */
@CrossOrigin
@RestController
@RequestMapping(value = "test/")
@RequiredArgsConstructor
public class test {


    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public String userTest() {

        return "helloWord";
    }


}
