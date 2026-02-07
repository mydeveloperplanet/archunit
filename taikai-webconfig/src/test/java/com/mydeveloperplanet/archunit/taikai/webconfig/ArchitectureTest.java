package com.mydeveloperplanet.archunit.taikai.webconfig;

import java.util.List;

import com.enofex.taikai.Taikai;
import com.enofex.taikai.TaikaiRule;
import com.mydeveloperplanet.archunit.taikai.webconfig.config.WebConfig;

import org.junit.jupiter.api.Test;

class ArchitectureTest {

    @Test
    void genericExceptions() {
        Taikai.builder()
                .namespace("com.mydeveloperplanet.archunit.taikai.webconfig")

                .java(java -> java
                        .methodsShouldNotDeclareGenericExceptions()
//                        .methodsShouldNotDeclareGenericExceptions(TaikaiRule.Configuration.of(List.of(WebConfig.class)))
                )

                .build()
                .check();
    }

}
