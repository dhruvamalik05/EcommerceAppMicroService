package com.demo.authcodeflow;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    private final Service2Class client;

    public HomeController(Service2Class client) {
        this.client = client;
    }

    @GetMapping("/proxy")
    public String home() {
        return client.fetchData();
    }

}
