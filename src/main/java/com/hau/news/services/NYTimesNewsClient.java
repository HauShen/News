package com.hau.news.services;

import com.hau.news.config.FeignLoggingConfig;
import com.hau.news.interceptors.FeignInterceptor;
import com.hau.news.responsebodies.NyTimesNewsResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "nytimes-client",url = "https://api.nytimes.com/svc/topstories/v2",configuration = FeignInterceptor.class)
public interface NYTimesNewsClient {

    /* @GetMapping("/home.json") //https://api.nytimes.com/svc/topstories/v2/home.json
     NyTimesNewsResponseBody getTopStories(@RequestParam("api-key") String apiKey);*/
     //https://api.nytimes.com/svc/topstories/v2/home.json?api-key = 1wFZncc9sJF7FWGauLpWSkhkCPurhUtz2qzxrP3zAGUN29nw

     @GetMapping("/science.json")
     NyTimesNewsResponseBody getScienceTopStories(@RequestParam("api-key") String apiKey);

     @GetMapping("/home.json")
     NyTimesNewsResponseBody getTopStories();

}
