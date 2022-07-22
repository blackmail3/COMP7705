package hku.cs.controller;

import cn.hutool.core.map.MapUtil;
import hku.cs.common.lang.Result;
import hku.cs.entity.Task;
import hku.cs.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class TestController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    TaskService taskService;

    /**
     * password generator - white list
     *
     * @return encoded password
     */
    @GetMapping("/test")
    public Result test() {
        String pass = bCryptPasswordEncoder.encode("123456");
        System.out.println(pass);
        return Result.succ(MapUtil.builder()
                .put("pass", pass)
                .build()
        );
    }

    @GetMapping("/test/pass")
    public Result passEncode() {
        String pass = bCryptPasswordEncoder.encode("111111");
        boolean matches = bCryptPasswordEncoder.matches("111111", pass);
        System.out.println(matches);

        return Result.succ(MapUtil.builder()
                .put("pass", pass)
                .put("matches", matches)
                .build()
        );
    }

    @PostMapping("/train")
    public Result train(@RequestParam String status, @RequestParam String task_id) {
        Long tid = Long.parseLong(task_id);
        // Receive algorithm side message...
        System.out.println(tid);
        Task task = taskService.getById(tid);
        if (task == null) {
            return Result.fail("fail...");
        }
        if (status.equals("finish")) {
            task.setStatus(2);
        } else {
            // if training fail...
            task.setStatus(3);
        }
        task.setEndTime(LocalDateTime.now());
        taskService.updateById(task);
        return Result.succ(status);
    }
}
