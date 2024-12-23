package com.prosigliere.blogapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prosigliere.blogapi.model.AllBlogPostDTO;
import com.prosigliere.blogapi.model.BlogPost;
import com.prosigliere.blogapi.model.Comment;
import com.prosigliere.blogapi.model.Header;
import com.prosigliere.blogapi.model.Response;
import com.prosigliere.blogapi.model.SingleBlogPostDTO;
import com.prosigliere.blogapi.service.BlogPostService;

@RestController
@RequestMapping("/api/posts")
public class BlogPostController {

    private BlogPostService blogPostService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping
    public ResponseEntity<Response<List<AllBlogPostDTO>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AllBlogPostDTO> posts = blogPostService.getAllPosts(page, size);
        return ResponseEntity.ok(new Response<>(new Header(
                HttpStatus.OK,
                posts.getNumberOfElements(),
                posts.getTotalPages(),
                page),
                posts.getContent()));
    }

    @PostMapping
    public ResponseEntity<Response<BlogPost>> createPost(@RequestBody BlogPost blogPost) {
        BlogPost post = blogPostService.createPost(blogPost);
        return new ResponseEntity<>(new Response<>(new Header(HttpStatus.CREATED), post), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<SingleBlogPostDTO>> getPostById(@PathVariable Long id) {
        SingleBlogPostDTO post = blogPostService.getPostById(id);
        return ResponseEntity.ok(new Response<>(new Header(HttpStatus.OK), post));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Response<Comment>> addCommentToPost(@PathVariable Long id, @RequestBody Comment comment) {
        Comment createdComment = blogPostService.addCommentToPost(id, comment);
        return ResponseEntity.ok(new Response<>(new Header(HttpStatus.OK), createdComment));
    }
}
