package com.hau.news.services;

import com.hau.news.models.NYTimesArticle;
import com.hau.news.requestbodies.ArticleRequestBody;
import com.hau.news.requestbodies.ArticleUpdatedRequestBody;
import com.hau.news.responsebodies.ArticleResponseBody;
import com.hau.news.responsebodies.ArticleUpdatedResponseBody;

public interface ArticleService {
    ArticleResponseBody createArticleByUserId(String userId,ArticleRequestBody articleRequestBody);
    ArticleResponseBody getArticleByOid(Long oid);
    ArticleUpdatedResponseBody editArticleByOid(Long oid, ArticleUpdatedRequestBody articleUpdatedRequestBody);
    String deleteArticleByOid(Long oid);



}
