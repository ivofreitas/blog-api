INSERT INTO blog_posts (title, content) VALUES
                                            ('First Post', 'This is the content of the first post.'),
                                            ('Second Post', 'This is the content of the second post.');

INSERT INTO comments (text, blog_post_id) VALUES
                                              ('Great post!', 1),
                                              ('Very informative.', 1),
                                              ('Looking forward to more.', 2);