package com.prosigliere.blogapi.exception;

public class BlogPostNotFoundException extends RuntimeException{

    public BlogPostNotFoundException() {
        super("Blog post not found");
    }

    public BlogPostNotFoundException(String message) {
        super(message);
    }

    public BlogPostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
