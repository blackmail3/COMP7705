package hku.cs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import hku.cs.common.lang.Result;
import hku.cs.entity.Model;
import hku.cs.entity.ModelConfig;
import hku.cs.entity.User;
import hku.cs.service.ModelConfigService;
import hku.cs.service.ModelService;
import hku.cs.service.UserService;
import hku.cs.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/model")
public class ModelController {
    @Autowired
    ModelService modelService;
    @Autowired
    UserService userService;
    @Autowired
    ModelConfigService modelConfigService;
    @Autowired
    RedisUtil redisUtil;

    @PostMapping("/add")
    public Result add(@RequestBody Model model){
        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long userid = user.getId();
        model.setUserId(userid);
        model.setUpdateTime(LocalDateTime.now());
        modelService.saveOrUpdate(model);
        Object object = redisUtil.get("model_config_"+userid);
        ModelConfig modelConfig = new ModelConfig(); // default value
//        System.out.println(object.toString());

        if (object==null) {
            modelConfig.setModelId(model.getModelId());
            modelConfigService.save(modelConfig);
        }
        else{
            modelConfig = JSON.parseObject((String) object, ModelConfig.class);
            modelConfig.setModelId(model.getModelId());
            modelConfigService.save(modelConfig);
        }
        redisUtil.del("model_config_"+userid);
        ArrayList<Object> modelList = new ArrayList<>();
        modelList.add(model);
        modelList.add(modelConfig);
        return Result.succ(modelList);
    }

    @GetMapping("/list")
    public Result models(){
        List<Model> models = modelService.getByuserId();
        return Result.succ(models);
    }

    @DeleteMapping("/del")
    public Result del(@RequestParam Long modelId){
        boolean del = modelService.removeById(modelId);
        return Result.succ(modelId);
    }
}
