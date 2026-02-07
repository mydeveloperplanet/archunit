package com.mydeveloperplanet.archunit.taikai.springconfig.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("app")
//@Configuration
public record ApplicationConfig(boolean enabled, String remoteServer, int remotePort) {
}
