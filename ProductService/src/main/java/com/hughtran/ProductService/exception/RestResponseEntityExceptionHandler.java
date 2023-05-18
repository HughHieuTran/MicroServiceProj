package com.hughtran.ProductService.exception;

import com.hughtran.ProductService.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductServiceCustomException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceException(
            ProductServiceCustomException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), e.getErrorCode()), HttpStatus.BAD_REQUEST);
    }
}
