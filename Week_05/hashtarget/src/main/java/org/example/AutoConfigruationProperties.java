package org.example;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("target.string")
public class AutoConfigruationProperties {
    private String target;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
