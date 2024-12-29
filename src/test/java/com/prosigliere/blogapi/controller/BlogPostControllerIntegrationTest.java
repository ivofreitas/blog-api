package com.prosigliere.blogapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
class BlogPostControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @Sql(scripts = "/test-data.sql")
    void givenPosts_whenGetAllPosts_thenReturnsPaginatedPosts() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.total").value(2))
                .andExpect(jsonPath("$.header.total_pages").value(1))
                .andExpect(jsonPath("$.header.page").value(0))
                .andExpect(jsonPath("$.result[0].title").value("First Post"))
                .andExpect(jsonPath("$.result[0].comment_count").value(2))
                .andExpect(jsonPath("$.result[1].title").value("Second Post"))
                .andExpect(jsonPath("$.result[1].comment_count").value(1));
    }

    @Test
    @Sql(scripts = "/test-data.sql")
    void givenPostId_whenGetPostById_thenReturnsPostDetails() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.status").value("OK"))
                .andExpect(jsonPath("$.header.page").doesNotExist())
                .andExpect(jsonPath("$.result.title").value("First Post"))
                .andExpect(jsonPath("$.result.content").value("This is the content of the first post."))
                .andExpect(jsonPath("$.result.comments[0].text").value("Great post!"))
                .andExpect(jsonPath("$.result.comments[1].text").value("Very informative."));
    }

    @Test
    void givenValidPost_whenCreatePost_thenReturnsCreatedPost() throws Exception {
        String newPostJson = """
    {
        "title": "New Blog Post",
        "content": "This is the content of the new blog post."
    }
    """;

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPostJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.status").value("CREATED"))
                .andExpect(jsonPath("$.result.title").value("New Blog Post"))
                .andExpect(jsonPath("$.result.content").value("This is the content of the new blog post."));
    }

    @Test
    @Sql(scripts = "/test-data.sql")
    void givenValidComment_whenAddCommentToPost_thenReturnsCreatedComment() throws Exception {
        String newCommentJson = """
    {
        "text": "This is a new comment."
    }
    """;

        mockMvc.perform(post("/api/posts/{id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCommentJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.status").value("CREATED"))
                .andExpect(jsonPath("$.result.text").value("This is a new comment."));
    }

    @Test
    void givenInvalidPostId_whenGetPostById_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Blog post not found"))
                .andExpect(jsonPath("$.internal_message").value("Blog post with ID 999 not found"));
    }

    @Test
    void givenInvalidPostId_whenAddCommentToPost_thenReturnsNotFound() throws Exception {
        String newCommentJson = """
    {
        "text": "This is a new comment."
    }
    """;

        mockMvc.perform(post("/api/posts/{id}/comments", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCommentJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.internal_message").value("Blog post with ID 999 not found"));
    }

}
