package com.hau.news.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NYTimesArticle {
    private String title;
    @JsonProperty("abstract")
    private String abstractText;
    private String url;
}
