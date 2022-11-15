package com.tuthien.backend.controller;

import com.tuthien.backend.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseModel internalServerError(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Có lỗi xảy ra trong quá trình xử lý");
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseModel validateError(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseModel(HttpStatus.BAD_REQUEST, null, ex.getMessage());
    }
}
