package com.dfs.switchsimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.*;
import org.springframework.boot.web.servlet.support.*;
import org.springframework.context.annotation.*;
import org.springframework.web.*;


@SpringBootApplication
@ComponentScan(basePackages = "com.dfs.switchsimulator.*")
public class SwitchSimulatorApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SwitchSimulatorApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SwitchSimulatorApplication.class);
    }

}
