package com.korai.study02.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Study02", description = "List / Map 응답(JSON 변환) 연습 API")
@RequestMapping("/api/study02")
@RestController
public class StudyController02 {

    // ===================== List 응답 =====================

    @Operation(summary = "문자열 List 응답",
            description = "List<String>을 반환하면 JSON 배열로 변환됩니다. 예) [\"사과\", \"바나나\", \"포도\"]")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    @GetMapping("/list/string")
    public List<String> getStringList() {
        List<String> fruits = new ArrayList<>();
        fruits.add("사과");
        fruits.add("바나나");
        fruits.add("포도");
        return fruits;
    }

    @Operation(summary = "숫자 List 응답",
            description = "List<Integer>를 반환하면 숫자 JSON 배열로 변환됩니다. 예) [10, 20, 30]")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    @GetMapping("/list/number")
    public List<Integer> getNumberList() {
        // List.of() : 불변(수정 불가) 리스트를 간단히 생성
        return List.of(10, 20, 30, 40, 50);
    }

    // ===================== Map 응답 =====================

    @Operation(summary = "기본 Map 응답",
            description = "Map<String, Object>를 반환하면 JSON 객체로 변환됩니다. key → JSON 필드명, value → JSON 값")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    @GetMapping("/map")
    public Map<String, Object> getMap() {
        // HashMap은 key 순서를 보장하지 않음
        Map<String, Object> user = new HashMap<>();
        user.put("name", "홍길동");
        user.put("age", 20);
        user.put("isStudent", true);
        return user;
    }

    @Operation(summary = "순서가 유지되는 Map 응답",
            description = "LinkedHashMap은 put 한 순서대로 JSON 필드가 출력됩니다. HashMap 응답과 비교해 보세요.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    @GetMapping("/map/ordered")
    public Map<String, Object> getOrderedMap() {
        Map<String, Object> user = new LinkedHashMap<>();
        user.put("id", 1);
        user.put("name", "홍길동");
        user.put("email", "hong@example.com");
        user.put("age", 20);
        return user;
    }

    // ===================== List + Map 조합 =====================

    @Operation(summary = "Map이 담긴 List 응답",
            description = "List<Map<String, Object>>를 반환하면 JSON 객체 배열로 변환됩니다. (게시글 목록 같은 형태)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    @GetMapping("/list/map")
    public List<Map<String, Object>> getListOfMap() {
        List<Map<String, Object>> users = new ArrayList<>();

        Map<String, Object> user1 = new LinkedHashMap<>();
        user1.put("id", 1);
        user1.put("name", "홍길동");
        users.add(user1);

        Map<String, Object> user2 = new LinkedHashMap<>();
        user2.put("id", 2);
        user2.put("name", "김철수");
        users.add(user2);

        Map<String, Object> user3 = new LinkedHashMap<>();
        user3.put("id", 3);
        user3.put("name", "이영희");
        users.add(user3);

        return users;
    }

    @Operation(summary = "List가 담긴 Map 응답",
            description = "Map 안에 List와 Map을 중첩해서 넣으면 중첩된 JSON 구조가 만들어집니다. (실무 API 응답 형태)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    @GetMapping("/map/list")
    public Map<String, Object> getMapWithList() {
        List<String> hobbies = List.of("독서", "운동", "코딩");

        Map<String, Object> address = new LinkedHashMap<>();
        address.put("city", "부산");
        address.put("zipCode", "12345");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("name", "홍길동");
        response.put("hobbies", hobbies);   // List → JSON 배열
        response.put("address", address);   // Map  → JSON 객체
        response.put("count", hobbies.size());
        return response;
    }
}
