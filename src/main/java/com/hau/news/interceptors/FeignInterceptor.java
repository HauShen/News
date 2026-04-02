package com.hau.news.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template){
        template.query("api-key", "1wFZncc9sJF7FWGauLpWSkhkCPurhUtz2qzxrP3zAGUN29nw");
    }

}
