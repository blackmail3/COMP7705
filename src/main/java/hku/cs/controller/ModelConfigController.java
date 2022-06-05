package hku.cs.controller;


import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import hku.cs.common.lang.Result;
import hku.cs.entity.ModelConfig;
import hku.cs.entity.User;
import hku.cs.service.ModelConfigService;
import hku.cs.service.UserService;
import hku.cs.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/model_config")
@CrossOrigin
public class ModelConfigController {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ModelConfigService modelConfigService;
    @Autowired
    UserService userService;

    @PostMapping("/add")
    public Result add(@RequestBody ModelConfig modelConfig){
        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long userid = user.getId();
        Object ob = JSONUtil.parse(modelConfig);
        redisUtil.set("modelconfig_"+userid, ob.toString());
        return Result.succ(modelConfig);
    }

}
