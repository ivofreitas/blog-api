package com.prosigliere.blogapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.prosigliere.blogapi.model.AllBlogPostDTO;
import com.prosigliere.blogapi.model.BlogPost;
import com.prosigliere.blogapi.model.Comment;
import com.prosigliere.blogapi.model.SingleBlogPostDTO;
import com.prosigliere.blogapi.repository.BlogPostRepository;
import com.prosigliere.blogapi.repository.CommentRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BlogPostService {

    private BlogPostRepository blogPostRepository;

    private CommentRepository commentRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository, CommentRepository commentRepository) {
        this.blogPostRepository = blogPostRepository;
        this.commentRepository = commentRepository;
    }

    public Page<AllBlogPostDTO> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPost> blogPosts = blogPostRepository.findAll(pageable);

        if (blogPosts.isEmpty()) {
            throw new EntityNotFoundException("No blog posts found");
        }

        List<AllBlogPostDTO> allBlogPosts = blogPosts
                .stream()
                .map(blogPost -> new AllBlogPostDTO(blogPost.getTitle(), blogPost.getCommentsCount()))
                .collect(Collectors.toList());

        return new PageImpl<>(allBlogPosts, pageable, blogPosts.getTotalElements());
    }

    public SingleBlogPostDTO getPostById(Long id) {
        Optional<BlogPost> optionalPost = blogPostRepository.findById(id);
        if (optionalPost.isPresent()) {
            BlogPost post = optionalPost.get();
            return new SingleBlogPostDTO(post.getTitle(), post.getContent(), post.getComments());
        }

        throw new EntityNotFoundException("Blog post not found");
    }

    public BlogPost createPost(BlogPost blogPost) {
        return blogPostRepository.save(blogPost);
    }

    public Comment addCommentToPost(Long postId, Comment comment) {
        Optional<BlogPost> optionalPost = blogPostRepository.findById(postId);
        optionalPost.ifPresent(comment::setBlogPost);
        return commentRepository.save(comment);
    }
}

