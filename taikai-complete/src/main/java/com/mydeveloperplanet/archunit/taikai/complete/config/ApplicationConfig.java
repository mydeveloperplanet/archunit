package com.mydeveloperplanet.archunit.taikai.complete.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app")
public record ApplicationConfig(boolean enabled, String remoteServer, int remotePort) {
}
