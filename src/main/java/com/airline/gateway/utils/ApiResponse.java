package com.airline.gateway.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
public class ApiResponse<T> {
    private Boolean success;

    private int status;

    private String message;

    private T data;

    private List<String> errors = new ArrayList<>();

    private LocalDateTime timestamp;

    private String path;

    private String traceId;
}
