package com.prosigliere.blogapi.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    @JsonProperty(value = "header")
    private Header header;
    @JsonProperty(value = "result")
    private T body;
    @JsonProperty(value = "message")
    private String message;
    @JsonProperty(value = "internal_message")
    private String internalMessage;
    @JsonIgnore
    private String stackTraceMessage;

    public Response(HttpStatus status) {
        this.header = new Header(status);
    }

    public Response(Header header, T body) {
        this.header = header;
        this.body = body;
    }

    public Response(HttpStatus status, Throwable throwable) throws JsonProcessingException {
        this.header = new Header(status);
        this.message = "Unexpected error";
        this.internalMessage = throwable.getLocalizedMessage();
        this.stackTraceMessage = getStackTraceAsString(throwable);
    }

    public Response(HttpStatus status, String message, Throwable throwable) throws JsonProcessingException {
        this.header = new Header(status);
        this.message = message;
        this.internalMessage = throwable.getLocalizedMessage();
        this.stackTraceMessage = getStackTraceAsString(throwable);
    }

    private String getStackTraceAsString(Throwable throwable) throws JsonProcessingException{
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();

        final int limit = Math.min(stackTraceElements.length, 5);
        List<StackTrace> stackTraces = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            StackTraceElement element = stackTraceElements[i];
            stackTraces.add(new StackTrace(
                    element.getClassName(),
                    element.getMethodName(),
                    element.getFileName(),
                    element.getLineNumber()));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(stackTraces);
    }

    public Header getHeader() {
        return this.header;
    }

    record StackTrace(String className, String methodName, String fileName, int lineNumber) {
    }
}
