package com.airline.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class StartupListener {


    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String port;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {

        System.out.println();
        System.out.println("==================================================");
        System.out.println("🚀 " + applicationName + " is running");
        System.out.println("==================================================");
        System.out.println("URL         : http://localhost:" + port);
        System.out.println("Port        : " + port);
        System.out.println("Status      : READY");
        System.out.println("==================================================");
        System.out.println();
    }
}
