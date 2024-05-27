package com.example.demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @PostMapping("/api/devices/0000000006251005/queue")
    public String test(@RequestBody NetworkServerRequest networkServerRequest){
        System.out.println(networkServerRequest);
        return networkServerRequest.toString();
    }
}


