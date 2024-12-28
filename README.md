# Blog API

This project is a **Blog API** developed using **Spring Boot** with **Spring Data JPA** and **MySQL**. It provides endpoints for creating blog posts, viewing blog posts along with the number of associated comments, and adding comments to specific posts.

The API supports pagination for fetching blog posts, and the data is stored in a **MySQL** database.

## List of Dependencies
- **Docker/Docker-compose**: Install Docker by following the [official Docker installation guide](https://docs.docker.com/get-docker/).

## Local Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/ivofreitas/blog-api
   cd blog-api
   ```
2. Run the application

```bash
docker-compose up --build
```

This will start the server at http://localhost:8080.
Start Prometheus at http://localhost:9090.
Start Grafana at http://localhost:3000 (login: admin, password: admin).

## Grafana Dashboard

1. Open http://localhost:3000 in your browser.
2. Navigate to the Spring Boot Observability dashboard.
3. Observe metrics and trends in real time.

## Endpoints
1. GET /api/posts
   This endpoint retrieves all blog posts with their titles and the number of comments associated with each post. The response is paginated.

Example cURL Request:
```bash
curl --location 'http://localhost:8080/api/posts?page=0&size=3'
```
Response:
```json
{
  "header": {
    "timestamp": "12/23/2024 10:48:01",
    "status": "OK",
    "total": 3,
    "total_pages": 1,
    "page": 0
  },
  "result": [
    {
      "title": "First Post",
      "comment_count": 3
    },
    {
      "title": "Second Post",
      "comment_count": 1
    },
    {
      "title": "Third Post",
      "comment_count": 0
    }
  ]
}
```

2. POST /api/posts
   This endpoint allows you to create a new blog post. You need to send the blog post's title and content in the request body.

Example cURL Request:
```bash
curl --location 'http://localhost:8080/api/posts' \
--header 'Content-Type: application/json' \
--data '{
"title": "Third Post",
"content": "This is the content of the third post."
}'
```
Response:
```json
{
    "header": {
        "timestamp": "12/23/2024 10:03:51",
        "status": "CREATED"
    },
    "result": {
        "id": 3,
        "title": "Third Post",
        "content": "This is the content of the third post."
    }
}
```

3. GET /api/posts/{id}
   This endpoint retrieves a specific blog post by its ID, including its title, content, and a list of associated comments.

Example cURL Request:
```bash
curl --location 'http://localhost:8080/api/posts/1'
````
Response:
```json
{
    "header": {
        "timestamp": "12/23/2024 10:47:15",
        "status": "OK"
    },
    "result": {
        "title": "First Post",
        "content": "This is the content of the first post.",
        "comments": [
            {
                "text": "Great post!"
            },
            {
                "text": "Very informative."
            },
            {
                "text": "Excellent!"
            }
        ]
    }
}
```

4. POST /api/posts/{id}/comments
   This endpoint allows you to add a comment to a specific blog post. You need to specify the id of the blog post and send the comment's text in the request body.

Example cURL Request:
```bash
curl --location 'http://localhost:8080/api/posts/1/comments' \
--header 'Content-Type: application/json' \
--data '{
"text": "Excellent!"
}'
```
Response:
```json
{
  "header": {
    "timestamp": "12/23/2024 10:47:03",
    "status": "CREATED"
  },
  "result": {
    "text": "Excellent!"
  }
}
```

## Next steps
1. Input validation
2. Unit/Integration testing
3. Cloud setup
4. CI/CD automation
5. API Documentation
6. Rate limiting
7. Backup/Disaster recovery
8. Security (authentication, authorization)
