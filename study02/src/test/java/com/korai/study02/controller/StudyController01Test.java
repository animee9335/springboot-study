package com.korai.study02.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudyController01Test {

    @Autowired
    MockMvc mockMvc;

    /** 응답 본문을 UTF-8로 읽어온다 (한글 응답 깨짐 방지) */
    private static String bodyOf(ResultActions result) throws Exception {
        return result.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @Nested
    @DisplayName("API 호출 테스트")
    class ApiCall {

        @Test
        @DisplayName("GET /api/study/a → 'GET 응답!'")
        void getTest() throws Exception {
            ResultActions result = mockMvc.perform(get("/api/study/a"))
                    .andExpect(status().isOk());
            assertThat(bodyOf(result)).isEqualTo("GET 응답!");
        }

        @Test
        @DisplayName("POST /api/study/b → 'POST 응답!'")
        void postTest() throws Exception {
            ResultActions result = mockMvc.perform(post("/api/study/b"))
                    .andExpect(status().isOk());
            assertThat(bodyOf(result)).isEqualTo("POST 응답!");
        }

        @Test
        @DisplayName("PUT /api/study/c → 'PUT 응답!'")
        void putTest() throws Exception {
            ResultActions result = mockMvc.perform(put("/api/study/c"))
                    .andExpect(status().isOk());
            assertThat(bodyOf(result)).isEqualTo("PUT 응답!");
        }

        @Test
        @DisplayName("DELETE /api/study/d → 'DELETE 응답!'")
        void deleteTest() throws Exception {
            ResultActions result = mockMvc.perform(delete("/api/study/d"))
                    .andExpect(status().isOk());
            assertThat(bodyOf(result)).isEqualTo("DELETE 응답!");
        }

    }

    @Nested
    @DisplayName("잘못된 요청 테스트")
    class BadRequest {

        /** 각 경로에 허용되지 않은 HTTP 메서드로 요청 → 400 + Allow 헤더에 허용 메서드 안내 */
        @ParameterizedTest(name = "{0} {1} → 400 (허용: {2})")
        @CsvSource({
                "POST,   /api/study/a, GET",
                "PUT,    /api/study/a, GET",
                "DELETE, /api/study/a, GET",
                "GET,    /api/study/b, POST",
                "PUT,    /api/study/b, POST",
                "DELETE, /api/study/b, POST",
                "GET,    /api/study/c, PUT",
                "POST,   /api/study/c, PUT",
                "DELETE, /api/study/c, PUT",
                "GET,    /api/study/d, DELETE",
                "POST,   /api/study/d, DELETE",
                "PUT,    /api/study/d, DELETE",
        })
        void wrongMethodTest(String method, String url, String allowed) throws Exception {
            ResultActions result = mockMvc.perform(request(HttpMethod.valueOf(method), url))
                    .andExpect(status().isBadRequest())
                    .andExpect(header().string(HttpHeaders.ALLOW, allowed));
            assertThat(bodyOf(result))
                    .isEqualTo("잘못된 요청입니다: " + method + " 메서드는 지원하지 않습니다.");
        }

        @ParameterizedTest(name = "GET {0} → 404")
        @ValueSource(strings = {
                "/api/study/x",       // 없는 하위 경로
                "/api/study",         // 상위 경로만 호출
                "/api/study/a/extra", // 경로 뒤에 더 붙은 경우
                "/api/studyy/a",      // 오타
        })
        @DisplayName("존재하지 않는 경로로 요청하면 404 Not Found")
        void notFoundTest(String url) throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("PATCH 처럼 매핑이 없는 메서드로 요청하면 400")
        void patchTest() throws Exception {
            mockMvc.perform(patch("/api/study/a"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("OpenAPI 문서 테스트")
    class OpenApiDocs {

        @Test
        @DisplayName("/v3/api-docs 에 4개 엔드포인트가 HTTP 메서드별로 등록된다")
        void pathsTest() throws Exception {
            mockMvc.perform(get("/v3/api-docs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.openapi").exists())
                    .andExpect(jsonPath("$.paths['/api/study/a'].get").exists())
                    .andExpect(jsonPath("$.paths['/api/study/b'].post").exists())
                    .andExpect(jsonPath("$.paths['/api/study/c'].put").exists())
                    .andExpect(jsonPath("$.paths['/api/study/d'].delete").exists());
        }

        @Test
        @DisplayName("@Tag, @Operation 정보가 문서에 반영된다")
        void annotationsTest() throws Exception {
            mockMvc.perform(get("/v3/api-docs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tags[?(@.name == 'Study01')]").exists())
                    .andExpect(jsonPath("$.paths['/api/study/a'].get.summary").value("GET 요청"))
                    .andExpect(jsonPath("$.paths['/api/study/b'].post.summary").value("POST 요청"))
                    .andExpect(jsonPath("$.paths['/api/study/c'].put.summary").value("PUT 요청"))
                    .andExpect(jsonPath("$.paths['/api/study/d'].delete.summary").value("DELETE 요청"));
        }

        @Test
        @DisplayName("잘못된 요청(400) 응답 설명이 문서에 반영된다")
        void errorResponsesTest() throws Exception {
            mockMvc.perform(get("/v3/api-docs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.paths['/api/study/a'].get.responses['400']").exists())
                    .andExpect(jsonPath("$.paths['/api/study/b'].post.responses['400']").exists())
                    .andExpect(jsonPath("$.paths['/api/study/c'].put.responses['400']").exists())
                    .andExpect(jsonPath("$.paths['/api/study/d'].delete.responses['400']").exists());
        }

        @Test
        @DisplayName("Swagger UI 경로(/doc)로 접근하면 UI 페이지로 리다이렉트된다")
        void swaggerUiTest() throws Exception {
            mockMvc.perform(get("/doc"))
                    .andExpect(status().is3xxRedirection());
        }
    }
}
