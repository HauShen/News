package com.hau.news.services;

import com.hau.news.models.UserProfile;
import com.hau.news.requestBodies.ArticleRequestBody;
import com.hau.news.requestBodies.ArticleUpdatedRequestBody;
import com.hau.news.responseBodies.ArticleResponseBody;
import com.hau.news.responseBodies.ArticleUpdatedResponseBody;

public interface ArticleService {
    ArticleResponseBody createArticleByUserId(String userId,ArticleRequestBody articleRequestBody);
    ArticleResponseBody getArticleByOid(Long oid);
    ArticleUpdatedResponseBody editArticleByOid(Long oid, ArticleUpdatedRequestBody articleUpdatedRequestBody);
    String deleteArticleByOid(Long oid);

}
