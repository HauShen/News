package com.hau.news.interceptors;

import com.hau.news.externalapiproperties.NYTimesProperty;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignInterceptor implements RequestInterceptor {
    private final NYTimesProperty nyTimesProperty;
    public FeignInterceptor(NYTimesProperty nyTimesProperty){
        this.nyTimesProperty = nyTimesProperty;
    }
    @Override
    public void apply(RequestTemplate template){
        template.query("api-key", nyTimesProperty.getKey());
    }

}
