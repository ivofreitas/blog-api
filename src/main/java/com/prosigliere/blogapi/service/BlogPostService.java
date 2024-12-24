package com.prosigliere.blogapi.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.prosigliere.blogapi.exception.BlogPostNotFoundException;
import com.prosigliere.blogapi.model.AllBlogPostDTO;
import com.prosigliere.blogapi.model.BlogPost;
import com.prosigliere.blogapi.model.Comment;
import com.prosigliere.blogapi.model.SingleBlogPostDTO;
import com.prosigliere.blogapi.repository.BlogPostRepository;
import com.prosigliere.blogapi.repository.CommentRepository;

@Service
public class BlogPostService {

    private BlogPostRepository blogPostRepository;

    private CommentRepository commentRepository;

    public BlogPostService(BlogPostRepository blogPostRepository, CommentRepository commentRepository) {
        this.blogPostRepository = blogPostRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * Retrieves all blog posts with pagination.
     *
     * @param page the page number to retrieve
     * @param size the number of items per page
     * @return a Page of AllBlogPostDTO containing blog post summaries
     */
    public Page<AllBlogPostDTO> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPost> blogPosts = blogPostRepository.findAll(pageable);

        if (blogPosts.isEmpty()) {
            return Page.empty(pageable);
        }

        List<AllBlogPostDTO> allBlogPosts = blogPosts
                .stream()
                .map(blogPost -> new AllBlogPostDTO(blogPost.getTitle(), blogPost.getCommentsCount()))
                .toList();

        return new PageImpl<>(allBlogPosts, pageable, blogPosts.getTotalElements());
    }

    /**
     * Retrieves a single blog post by its ID.
     *
     * @param id the ID of the blog post to retrieve
     * @return a SingleBlogPostDTO containing detailed information about the blog post
     * @throws BlogPostNotFoundException if the blog post with the given ID is not found
     */
    public SingleBlogPostDTO getPostById(Long id) {
        BlogPost post = blogPostRepository.findById(id).orElseThrow(BlogPostNotFoundException::new);;
        return new SingleBlogPostDTO(post.getTitle(), post.getContent(), post.getComments());
    }

    /**
     * Creates a new blog post.
     *
     * @param blogPost the BlogPost entity to save
     * @return the saved BlogPost entity
     */
    public BlogPost createPost(BlogPost blogPost) {
        return blogPostRepository.save(blogPost);
    }

    /**
     * Adds a comment to a blog post.
     *
     * @param postId the ID of the blog post to add the comment to
     * @param comment the Comment entity to add
     * @return the saved Comment entity
     * @throws BlogPostNotFoundException if the blog post with the given ID is not found
     */
    public Comment addCommentToPost(Long postId, Comment comment) {
        BlogPost post = blogPostRepository.findById(postId).orElseThrow(BlogPostNotFoundException::new);
        comment.setBlogPost(post);
        return commentRepository.save(comment);
    }
}

