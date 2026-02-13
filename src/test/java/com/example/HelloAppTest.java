package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloAppTest {
    @Test
    void greetReturnsHelloWorld() {
        assertEquals("Hello, World!", HelloApp.greet());
    }
}
