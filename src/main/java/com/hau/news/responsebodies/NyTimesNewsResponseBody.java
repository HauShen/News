package com.hau.news.responsebodies;

import com.hau.news.models.NYTimesArticle;
import lombok.Data;

import java.util.List;
@Data
public class NyTimesNewsResponseBody {
    private String status;
    private List<NYTimesArticle> results;

}
