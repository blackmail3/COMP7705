package hku.cs.controller;

import hku.cs.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
public class BaseController {
    @Autowired
    HttpServletRequest req;
    @Autowired
    RedisUtil redisUtil;
}