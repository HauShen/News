package com.hau.news.serviceimpls;

import com.hau.news.externalapiproperties.NYTimesProperty;
import com.hau.news.models.NYTimesArticle;
import com.hau.news.responsebodies.NyTimesNewsResponseBody;
import com.hau.news.services.NYTimesNewsClient;
import com.hau.news.services.NewsService;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class NYTimesNewsServiceImpl implements NewsService {
    private final NYTimesNewsClient nyTimesNewsClient;
    private final NYTimesProperty nyTimesProperty;

    public NYTimesNewsServiceImpl(NYTimesNewsClient nyTimesNewsClient, NYTimesProperty nyTimesProperty){
        this.nyTimesNewsClient = nyTimesNewsClient;
        this.nyTimesProperty = nyTimesProperty;
    }
    @Override
    public List<NYTimesArticle> getTopStories(){
        NyTimesNewsResponseBody nyTimesNewsResponseBody =  nyTimesNewsClient.getTopStories(nyTimesProperty.getKey());
        if(nyTimesNewsResponseBody == null || nyTimesNewsResponseBody.getResults() == null){
            return Collections.emptyList();
        }
        return nyTimesNewsResponseBody.getResults();
    }
    @Override
    public  List<NYTimesArticle> getScienceTopStories(){
        NyTimesNewsResponseBody nyTimesNewsResponseBody = nyTimesNewsClient.getScienceTopStories(nyTimesProperty.getKey());
        if(nyTimesNewsResponseBody == null || nyTimesNewsResponseBody.getResults() == null){
            return Collections.emptyList();
        }
        return nyTimesNewsResponseBody.getResults();
    }

}
