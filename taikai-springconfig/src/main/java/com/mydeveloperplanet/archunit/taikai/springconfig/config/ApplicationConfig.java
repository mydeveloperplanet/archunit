package com.mydeveloperplanet.archunit.taikai.springconfig.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app")
public record ApplicationConfig(boolean enabled, String remoteServer, int remotePort) {
}
