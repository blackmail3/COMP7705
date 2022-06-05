package hku.cs.controller;

import cn.hutool.core.map.MapUtil;
import hku.cs.common.lang.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * password generator - white list
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
    public Result train(@RequestParam String statu, @RequestParam String task_id){
        Long tid = Long.parseLong(task_id);
        //...

        return Result.succ(statu);
    }
}
