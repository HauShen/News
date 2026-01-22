package com.hau.news.requestBodies;

import lombok.Data;

@Data
public class ArticleUpdatedRequestBody {
    private String title;
    private String content;
    public ArticleUpdatedRequestBody(String title,String content){
        this.title = title;
        this.content = content;
    }

}
