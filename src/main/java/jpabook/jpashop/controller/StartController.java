package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class StartController {

    @RequestMapping("/")
    public String home() {
        log.info("home controller"); //로그찍기
        return "login";
    }
}
