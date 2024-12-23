# Blog API

This project is a **Blog API** developed using **Spring Boot** with **Spring Data JPA** and **MySQL**. It provides endpoints for creating blog posts, viewing blog posts along with the number of associated comments, and adding comments to specific posts.

The API supports pagination for fetching blog posts, and the data is stored in a **MySQL** database.

## List of Dependencies

- **Java 23**: Download and install Java 23 from the [official Java website](https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html) or use a version manager like **SDKMAN**.
- **Maven**: If Maven is not already installed, download it from [Maven's official site](https://maven.apache.org/download.cgi) and follow the installation instructions.
- **MySQL**: You can either install MySQL locally or use Docker (explained below). To install MySQL, follow the instructions from the [official MySQL website](https://dev.mysql.com/doc/refman/8.0/en/installing.html).
- **Docker/Docker-compose (Optional)**: Install Docker by following the [official Docker installation guide](https://docs.docker.com/get-docker/).

### Docker Setup (Optional)

You can use Docker to run a **MySQL** container instead of installing MySQL locally. Here is the command to run MySQL using Docker:

```bash
docker run --name mysql -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=blogdb -p 3306:3306 -d mysql:8
```

## Local Setup

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd <project-directory>
   ```
2. Install dependencies: If you're using Maven, you can install the necessary dependencies by running:

```bash
./mvnw clean install
```
3. Configure MySQL: Ensure that MySQL is running. 

```bash
docker-compose up -d
```
4. Run the application: Start the application using Maven:

```bash
mvn spring-boot:run
```
This will start the server at http://localhost:8080.

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
    "status": "OK"
  },
  "result": {
    "text": "Excellent!"
  }
}
```

## Next steps
1. Input validation
2. Unit/Integration testing
2. Observability (logs, metrics, traces)
3. Cloud setup
4. CI/CD automation
5. API Documentation