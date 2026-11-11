package com.korai.study02.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리.
 * 허용되지 않은 HTTP 메서드로 요청이 오면 기본값인 405 대신 400 Bad Request로 응답한다.
 * (메서드 불일치는 컨트롤러를 찾기 전에 발생하므로 컨트롤러 단위가 아닌 전역으로 처리해야 한다)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        HttpHeaders headers = new HttpHeaders();
        if (e.getSupportedHttpMethods() != null) {
            headers.setAllow(e.getSupportedHttpMethods()); // 어떤 메서드로 요청해야 하는지 안내
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .body("잘못된 요청입니다: " + e.getMethod() + " 메서드는 지원하지 않습니다.");
    }
}
