package com.boneacsu.research.discovery.eureka.consumer;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableFeignClients
@Controller
public class FeignClientApplication {
    @Autowired
    private GreetingClient greetingClient;

    @Autowired
    private EurekaClient discoveryClient;

    public static void main(String[] args) {
        SpringApplication.run(FeignClientApplication.class, args);
    }

    @RequestMapping("/get-greeting")
    public String greeting(Model model) {
        model.addAttribute("greeting", greetingClient.greeting());
        return "greeting-view";
    }

    @RequestMapping("/get-byMeta")
    public String greeting(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            Model model) {

        List<InstanceInfo> instances =
                discoveryClient.getApplications()
                        .getRegisteredApplications("spring-cloud-eureka-client")
                        .getInstances();

        List<String> instancesMatching = instances.stream()
                .filter((i) ->
                        i.getMetadata().containsKey(key) && i.getMetadata().get(key).equals(value))
                .map((i) -> i.getInstanceId())
                .collect(Collectors.toList());

        model.addAttribute("greeting", "List of filtered apps : " + instancesMatching.toString());
        return "greeting-view";
    }
}
