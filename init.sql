CREATE TABLE blog_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL
);

CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text TEXT NOT NULL,
    blog_post_id BIGINT,
    FOREIGN KEY (blog_post_id) REFERENCES blog_posts(id) ON DELETE CASCADE
);

INSERT INTO blog_posts (title, content) VALUES
    ('First Post', 'This is the content of the first post.'),
    ('Second Post', 'This is the content of the second post.');

INSERT INTO comments (text, blog_post_id) VALUES
    ('Great post!', 1),
    ('Very informative.', 1),
    ('Looking forward to more.', 2);
