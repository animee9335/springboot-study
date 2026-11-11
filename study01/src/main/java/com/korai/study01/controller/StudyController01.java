package com.korai.study01.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class StudyController01 {

    @Value("${user.dir}")
    private String projectDir;

//    @ResponseBody   // RestController가 존재하기 이전에 사용하던 방식
    @GetMapping("/api/get01")
    public String get01() {
        return "/index.html";      // '/'가 없으면 상대경로, ResponseBody가 없다면 return 값은 경로다
    }

    @GetMapping("/api/get02")
    public String get02() {
        System.out.println(projectDir + "/src/main/resources/upload");
        String uploadPath =  projectDir + "/src/main/resources/upload";
        return "/file:///" + uploadPath + "/asd.png";
    }

    @ResponseBody
    @GetMapping("/api/get03")
    public String get03() {
        return "데이터 응답!!";
    }


}
