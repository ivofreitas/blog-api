package com.prosigliere.blogapi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleBlogPostDTO {
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "content")
    private String content;
    @JsonProperty(value = "comments")
    private List<Comment> comments;

    public SingleBlogPostDTO(String title, String content, List<Comment> comments) {
        this.title = title;
        this.content = content;
        this.comments = comments;
    }
}
