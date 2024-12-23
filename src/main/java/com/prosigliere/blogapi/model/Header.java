package com.prosigliere.blogapi.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Header {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    @JsonProperty(value = "status")
    private HttpStatus status;
    @JsonProperty(value = "total")
    private Integer total;
    @JsonProperty(value = "total_pages")
    private Integer totalPages;
    @JsonProperty(value = "page")
    private Integer page;

    private Header(){
        timestamp = LocalDateTime.now();
    }

    public Header(HttpStatus status) {
        this();
        this.status = status;
    }

    public Header(HttpStatus status, Integer total, Integer totalPages, Integer page) {
        this();
        this.status = status;
        this.total = total;
        this.totalPages = totalPages;
        this.page = page;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getPage() {
        return page;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
