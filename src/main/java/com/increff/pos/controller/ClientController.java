package com.increff.pos.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class ClientController {
    @ApiOperation(value = "get")
    @RequestMapping(path = "/api/client", method = RequestMethod.GET)
    public String get() {
        return "Client is working";
    }
}
