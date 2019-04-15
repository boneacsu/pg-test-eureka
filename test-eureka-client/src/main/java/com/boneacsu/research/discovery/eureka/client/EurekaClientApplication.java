package com.boneacsu.research.discovery.eureka.client;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.EurekaClient;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class EurekaClientApplication implements GreetingController {
    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Autowired
    @Lazy
    private ApplicationInfoManager appInfoManager;

    @Value("${spring.application.name}")
    private String appName;

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    @Override
    public String assign()
    {
        String rand1 = RandomStringUtils.randomAlphabetic(16);
        String rand2 = RandomStringUtils.randomAlphabetic(16);
        Map<String, String> metaDataMap = appInfoManager.getEurekaInstanceConfig().getMetadataMap();

        metaDataMap.put("AppPropertyOne", rand1);
        metaDataMap.put("AppPropertyTwo", rand2);

        appInfoManager.registerAppMetadata(metaDataMap);

        return metaDataMap.toString();
    }

    @Override
    public String greeting() {
        String ret = String.format("Hello from '%s'!", eurekaClient.getApplication(appName).getName());

        Map<String, String> map = appInfoManager.getEurekaInstanceConfig().getMetadataMap();

        return ret + map;
    }
}
