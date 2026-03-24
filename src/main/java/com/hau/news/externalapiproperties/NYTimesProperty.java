package com.hau.news.externalapiproperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nytimes.api")

public class NYTimesProperty {
    private String key;
    private String baseUrl;

    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key = key;
    }

    public String getBaseUrl(){
        return baseUrl;
    }
    public void setBaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
    }


}
