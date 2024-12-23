package com.prosigliere.blogapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AllBlogPostDTO {
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "comment_count")
    private Integer commentCount;

    public AllBlogPostDTO(String title, Integer commentsCount) {
        this.title = title;
        this.commentCount = commentsCount;
    }
}
