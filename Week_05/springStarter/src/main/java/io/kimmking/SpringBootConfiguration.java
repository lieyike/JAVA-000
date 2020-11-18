package io.kimmking;

import io.kimmking.spring01.Student;
import io.kimmking.spring02.Klass;
import io.kimmking.spring02.School;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan("io.io.kimmking")
@EnableConfigurationProperties
@RequiredArgsConstructor
public class SpringBootConfiguration {

    //不知道這個要怎麽做裝配什麽

    @Autowired
    private AutoConfiurationProperties autoConfiurationProperties;

    @Bean
    @ConditionalOnMissingBean
    public School initSchool() {
        School school = new School(autoConfiurationProperties.getName());
        return school;
    }
}
