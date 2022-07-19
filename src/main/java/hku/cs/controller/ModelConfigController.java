package hku.cs.controller;


import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
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
    public Result add(@RequestBody ModelConfig modelConfig) {
        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long user_id = user.getId();
        System.out.println(modelConfig.toString());
        Object ob = new JSONObject();
        try {
            ob = JSONUtil.parse(modelConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.succ("Insert data exception" + e.getMessage());
        }
        System.out.println(ob.toString());
        redisUtil.set("model_config_" + user_id, ob.toString());
        return Result.succ(modelConfig);
    }

    @GetMapping("/{modelId}")
    public Result getConfig(@PathVariable Long modelId) {
        ModelConfig modelConfig = new ModelConfig();
        try {
            modelConfig = modelConfigService.getByModelId(modelId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.succ("Get data exception" + modelId);
        }
        return Result.succ(modelConfig);
    }

}
