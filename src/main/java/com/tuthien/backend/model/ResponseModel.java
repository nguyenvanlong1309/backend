package com.tuthien.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel<T> {

    private HttpStatus status;
    private T data;
    private String message;

    public ResponseModel(HttpStatus status, T data) {
        this(status, data, null);
    }
}
