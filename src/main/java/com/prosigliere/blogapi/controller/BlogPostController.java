package com.prosigliere.blogapi.controller;

import java.util.List;

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

import com.prosigliere.blogapi.exception.BlogPostNotFoundException;
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

    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    /**
     * Retrieves a paginated list of all blog posts.
     *
     * @param page the page number to retrieve, default is 0
     * @param size the size of the page, default is 10
     * @return a paginated list of blog posts with HTTP status 200 (OK)
     */
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

    /**
     * Creates a new blog post.
     *
     * @param blogPost the blog post to be created
     * @return the created blog post with HTTP status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Response<BlogPost>> createPost(@RequestBody BlogPost blogPost) {
        BlogPost post = blogPostService.createPost(blogPost);
        return new ResponseEntity<>(new Response<>(new Header(HttpStatus.CREATED), post), HttpStatus.CREATED);
    }

    /**
     * Retrieves a specific blog post by its ID.
     *
     * @param id the ID of the blog post to retrieve
     * @return the details of the blog post with HTTP status 200 (OK)
     * @throws BlogPostNotFoundException if the blog post with the given ID is not found with HTTP status 404 (NOT FOUND)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response<SingleBlogPostDTO>> getPostById(@PathVariable Long id) {
        SingleBlogPostDTO post = blogPostService.getPostById(id);
        return ResponseEntity.ok(new Response<>(new Header(HttpStatus.OK), post));
    }

    /**
     * Adds a comment to a specific blog post.
     *
     * @param id      the ID of the blog post to which the comment is added
     * @param comment the comment to be added
     * @return the created comment with HTTP status 201 (Created)
     * @throws BlogPostNotFoundException if the blog post with the given ID is not found with HTTP status 404 (NOT FOUND)
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<Response<Comment>> addCommentToPost(@PathVariable Long id, @RequestBody Comment comment) {
        Comment createdComment = blogPostService.addCommentToPost(id, comment);
        return new ResponseEntity<>(new Response<>(new Header(HttpStatus.CREATED), createdComment), HttpStatus.CREATED);
    }
}
