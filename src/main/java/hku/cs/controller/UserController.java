package hku.cs.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import hku.cs.common.dto.LoginDto;
import hku.cs.common.lang.Result;
import hku.cs.entity.User;
import hku.cs.service.UserService;
import hku.cs.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * User controller for register, login and user list.
 * @author xxy
 */
@RestController
@RequestMapping("/api/v1.0/user")
@CrossOrigin
@ApiOperation(value = "user_manage", notes = "API for user management, i.e. CRUD")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/list")
    public Object user_list(){
        return userService.list();
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        String pass = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(pass);
        userService.saveOrUpdate(user);
        return Result.succ(user);
    }

    @ApiOperation("After login, 'Authorization' should be added to request Headers.")
    @PostMapping("/login")
    public Result login(@RequestParam @ApiParam("username, 'admin for test.") String username,
                        @RequestParam @ApiParam("password, '123456' for test.") String password,
                        @RequestParam @ApiParam("Captcha Code, '123456' for test.") String code,
                        @RequestParam @ApiParam("jwt, 'aaaaa' for test.") String token){
        return Result.succ(MapUtil.builder()
                            .put("username", username));
    }
}
