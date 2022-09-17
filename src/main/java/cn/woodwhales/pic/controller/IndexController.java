package cn.woodwhales.pic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author woodwhales on 2022-09-17 21:35
 */
@Slf4j
@Validated
@RequestMapping("/")
@RestController
public class IndexController {

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping({"", "index"})
    public String index() {
        log.info("{}", appName);
        return appName;
    }

}
