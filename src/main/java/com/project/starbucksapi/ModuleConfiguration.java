package com.project.starbucksapi;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = {
        "br.com.project.starbuckapi"
})
public class ModuleConfiguration {

}
