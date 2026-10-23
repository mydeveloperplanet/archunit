package com.mydeveloperplanet.archunit.taikai.complete;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DummyTest {

    @Test
    void testSomethingWithAssert() {
        assertThat(true).isTrue();
    }

    @Test
    void testSomethingWithHelperMethodAssert() {
        assertHelperMethod();
    }

    private static void assertHelperMethod() {
        assertThat(true).isTrue();
    }

}
