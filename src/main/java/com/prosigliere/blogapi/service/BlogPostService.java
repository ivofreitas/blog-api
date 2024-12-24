package com.prosigliere.blogapi.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(BlogPostService.class);

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
        logger.info("Fetching all blog posts - Page: {}, Size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPost> blogPosts = blogPostRepository.findAll(pageable);

        if (blogPosts.isEmpty()) {
            logger.warn("No blog posts found for Page: {}, Size: {}", page, size);
            return Page.empty(pageable);
        }

        List<AllBlogPostDTO> allBlogPosts = blogPosts
                .stream()
                .map(blogPost -> new AllBlogPostDTO(blogPost.getTitle(), blogPost.getCommentsCount()))
                .toList();

        logger.info("Returning {} blog posts for Page: {}, Size: {}", allBlogPosts.size(), page, size);
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
        logger.info("Fetching blog post with ID: {}", id);

        BlogPost post = blogPostRepository.findById(id).orElseThrow(() -> {
            logger.error("Blog post with ID {} not found", id);
            return new BlogPostNotFoundException(String.format("Blog post with ID %d not found", id));
        });

        logger.info("Successfully retrieved blog post with ID: {}", id);
        return new SingleBlogPostDTO(post.getTitle(), post.getContent(), post.getComments());
    }

    /**
     * Creates a new blog post.
     *
     * @param blogPost the BlogPost entity to save
     * @return the saved BlogPost entity
     */
    public BlogPost createPost(BlogPost blogPost) {
        logger.info("Creating a new blog post with Title: {}", blogPost.getTitle());
        BlogPost post = blogPostRepository.save(blogPost);
        logger.info("Successfully created blog post with Title: {}", post.getTitle());
        return post;
    }

    /**
     * Adds a comment to a blog post.
     *
     * @param postID the ID of the blog post to add the comment to
     * @param comment the Comment entity to add
     * @return the saved Comment entity
     * @throws BlogPostNotFoundException if the blog post with the given ID is not found
     */
    public Comment addCommentToPost(Long postID, Comment comment) {
        logger.info("Adding a comment to blog post with ID: {}", postID);

        BlogPost post = blogPostRepository.findById(postID).orElseThrow(() -> {
            logger.error("Blog post with ID {} not found. Cannot add comment.", postID);
            return new BlogPostNotFoundException(String.format("Blog post with ID %d not found", postID));
        });
        comment.setBlogPost(post);

        Comment savedComment = commentRepository.save(comment);

        logger.info("Successfully added comment to blog post with ID: {}. Comment ID: {}", postID, savedComment.getID());
        return savedComment;
    }
}

