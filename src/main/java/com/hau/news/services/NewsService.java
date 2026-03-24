package com.hau.news.services;

import com.hau.news.models.NYTimesArticle;

import java.util.List;

public interface NewsService {
    List<NYTimesArticle> getTopStories();
    List<NYTimesArticle> getScienceTopStories();

}
