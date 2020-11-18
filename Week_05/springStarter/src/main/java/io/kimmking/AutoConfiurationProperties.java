package io.kimmking;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "school.name")
public class AutoConfiurationProperties {

    private String name;

    public String getName() {
        return name;
    }

    public void setNmae(String name) {
        this.name = name;
    }
}
