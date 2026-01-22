package com.hau.news.requestBodies;

import lombok.Data;

@Data
public class ArticleRequestBody {
    private String title;
    private String content;

    public ArticleRequestBody(String title, String content){
        this.title = title;
        this.content = content;
    }
}
