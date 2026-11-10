package com.demo.producer;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class KafkaProducerStreams {
    @Bean
    public Supplier<RiderLocation> sendRiderLocation () {
        return () -> {
            RiderLocation riderLocation = new RiderLocation("rider123", 16.7, 12.3);
            System.out.println(riderLocation);
            return riderLocation;
        };
    }

}
