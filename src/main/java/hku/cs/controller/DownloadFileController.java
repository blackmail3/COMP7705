package hku.cs.controller;

import hku.cs.service.TaskService;
import hku.cs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: 2022/7/12 click download button and download the log file
@RestController
@RequestMapping("/api/v1.0/download")
@CrossOrigin
public class DownloadFileController {
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;


}
