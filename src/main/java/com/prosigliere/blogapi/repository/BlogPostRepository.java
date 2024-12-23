package com.prosigliere.blogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prosigliere.blogapi.model.BlogPost;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
}
