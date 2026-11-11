package com.korai.study02.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Study01", description = "HTTP 메서드별 요청 매핑 연습 API")
@RequestMapping("/api/study")
@RestController
public class StudyController01 {

    private String test;

    @Operation(summary = "GET 요청", description = "GET 방식으로 호출하면 고정 문자열을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (허용되지 않은 HTTP 메서드로 요청한 경우)", content = @Content)
    })
    @GetMapping("/a")
    public String get() {
        return "GET 응답!";
    }

    @Operation(summary = "POST 요청", description = "POST 방식으로 호출하면 고정 문자열을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (허용되지 않은 HTTP 메서드로 요청한 경우)", content = @Content)
    })
    @PostMapping("/b")
    public String post() {
        return "POST 응답!";
    }

    @Operation(summary = "PUT 요청", description = "PUT 방식으로 호출하면 고정 문자열을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (허용되지 않은 HTTP 메서드로 요청한 경우)", content = @Content)
    })
    @PutMapping("/c")
    public String put() {
        return "PUT 응답!";
    }

    @Operation(summary = "DELETE 요청", description = "DELETE 방식으로 호출하면 고정 문자열을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (허용되지 않은 HTTP 메서드로 요청한 경우)", content = @Content)
    })
    @DeleteMapping("/d")
    public String delete() {
        return "DELETE 응답!";
    }
}
