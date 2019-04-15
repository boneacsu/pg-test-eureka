package com.boneacsu.research.discovery.eureka.client;

import org.springframework.web.bind.annotation.RequestMapping;

public interface GreetingController {
    @RequestMapping("/greeting")
    String greeting();

    @RequestMapping("/assign")
    String assign();
}
