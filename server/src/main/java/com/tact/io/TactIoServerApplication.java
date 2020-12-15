package com.tact.io;

import ch.qos.logback.core.status.NopStatusListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class TactIoServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TactIoServerApplication.class, args);
    }
}
