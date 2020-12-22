package com.sinjs.todoapi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="TODO API")
@RestController
public class IndexController {

    @ApiOperation("TODO API - Homepage")
    @GetMapping("/")
    public String index() {
        return "TODO API";
    }
}
