package hku.cs.controller;

import cn.hutool.core.lang.copier.SrcToDestCopier;
import cn.hutool.crypto.Mode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import hku.cs.common.lang.Result;
import hku.cs.entity.*;
import hku.cs.service.ModelConfigService;
import hku.cs.service.ModelService;
import hku.cs.service.UserService;
import hku.cs.util.RedisUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
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
@CrossOrigin
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
    public Result add(@RequestBody ModelParam modelParam) {
        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long userid = user.getId();
        Model model = new Model();

        model.setUserId(userid);
        model.setUpdateTime(LocalDateTime.now());
        model.setBasicModel(modelParam.getBasicModel());
        model.setPreTrainedModel(modelParam.getPreTrainedModel());
        model.setDescription(modelParam.getDescription());
        model.setModelName(modelParam.getModelName());
        model.setInitParam(modelParam.getInitParam());
        modelService.saveOrUpdate(model);

        Config config = modelParam.getConfig();
        ModelConfig modelConfig = new ModelConfig(); // default value
//        System.out.println(object.toString());

        if (config == null) {
            modelConfig.setModelId(model.getModelId());
            modelConfigService.saveOrUpdate(modelConfig);
        } else {
            System.out.println(config.toString());
//            Config config = JSON.parseObject((String) object, Config.class);
            modelConfig = Conf2MC(config);
            modelConfig.setModelId(model.getModelId());
            modelConfigService.save(modelConfig);
        }
        return Result.succ(modelParam);
    }

    @PutMapping("/update")
    public Result update(@RequestBody ModelParam modelParam) {
        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long userid = user.getId();
        Model model = new Model();

        System.out.println("MODEL_UPDATE" + modelParam.getModelId());

        model.setUserId(userid);
        model.setModelId(modelParam.getModelId());
        model.setUpdateTime(LocalDateTime.now());
        model.setBasicModel(modelParam.getBasicModel());
        model.setPreTrainedModel(modelParam.getPreTrainedModel());
        model.setDescription(modelParam.getDescription());
        model.setModelName(modelParam.getModelName());
        model.setInitParam(modelParam.getInitParam());
        modelService.updateById(model);
        System.out.println(model.toString());

        Config config = modelParam.getConfig();
        ModelConfig modelConfig = new ModelConfig(); // default value
//        System.out.println(object.toString());

        if (config == null) {
            modelConfig.setModelId(model.getModelId());
            modelConfigService.updateByModelId(model.getModelId(), modelConfig);
        } else {
            System.out.println(config.toString());
//            Config config = JSON.parseObject((String) object, Config.class);
            modelConfig = Conf2MC(config);
            modelConfig.setModelId(model.getModelId());
            modelConfigService.updateByModelId(model.getModelId(), modelConfig);
            if (modelConfigService.getByModelId(model.getModelId())==null){
                modelConfigService.save(modelConfig);
            }
            System.out.println(modelConfig.toString());
        }
        return Result.succ(modelParam);
    }

    @DeleteMapping("/del")
    public Result del(@RequestParam Long modelId) {
        boolean del = modelService.removeById(modelId);
        return Result.succ(del);
    }

    @GetMapping("/detail")
    public Result getById(@RequestParam Long modelId) {
        Model model = modelService.getById(modelId);
        return Result.succ(model);
    }

//    @GetMapping("/get")
//    public Result get(@RequestParam String name) {
//        List<Model> list = modelService.getByName(name);
//        return Result.succ(list);
//    }

    @GetMapping("/get")
    public Result getByIdName(@RequestParam @Nullable String model_id, @RequestParam @Nullable String name) {
        if (model_id != null && !model_id.equals("")) {
            Model model = modelService.getById(Long.parseLong(model_id));
            ModelConfig modelConfig = modelConfigService.getByModelId(Long.parseLong(model_id));
            Config config = MC2Conf(modelConfig);
            ModelParam modelParam = marshallParam(model, config);
            List<ModelParam> modelParams = new ArrayList<>();
            modelParams.add(modelParam);
            return Result.succ(modelParams);
        } else {
            if (name == null || name.equals("")) {
                name = "";
            }
            List<Model> list = new ArrayList<>(modelService.getByName(name));
            List<ModelParam> modelParams = new ArrayList<>();
            for (Model model : list) {
                ModelConfig modelConfig = modelConfigService.getByModelId(model.getModelId());
                Config config = MC2Conf(modelConfig);
                ModelParam modelParam = marshallParam(model, config);
                modelParams.add(modelParam);
            }
            return Result.succ(modelParams);
        }
    }

    public ModelParam marshallParam(Model model, Config config) {
        ModelParam param = new ModelParam();
        param.setBasicModel(model.getBasicModel());
        param.setDescription(model.getDescription());
        param.setInitParam(model.getInitParam());
        param.setConfig(config);
        param.setModelId(model.getModelId());
        param.setModelName(model.getModelName());
        param.setPreTrainedModel(model.getPreTrainedModel());
        param.setUpdateTime(model.getUpdateTime());
        param.setUserId(model.getUserId());
        return param;
    }

    public Config MC2Conf(ModelConfig modelConfig) {
        Config config = new Config();
        try {
            config.setActivation(modelConfig.getActivation());
        } catch (Exception e) {
            config.setActivation("relu");
        }
        try {
            config.setClsType(modelConfig.getClsType());
        } catch (Exception e) {
            config.setClsType("cls");
        }
        try {
            config.setLnType(modelConfig.getLnType());
        } catch (Exception e) {
            config.setClsType("cls");
        }
        try {
            config.setLyrType(modelConfig.getLyrType());
        } catch (Exception e) {
            config.setClsType("");
        }
        try {
            config.setPoolerType(modelConfig.getPoolerType());
        } catch (Exception e) {
            config.setClsType("");
        }
        try {
            String s = modelConfig.getFreeze();
        } catch (Exception e) {
            config.setFreeze(new ArrayList<>());
        }
        try {
            String[] freezes = modelConfig.getFreeze().split(",");
            List<Integer> list = new ArrayList<>();
            for (String freeze : freezes) {
                if (freeze.length() != 0)
                    list.add(Integer.parseInt(freeze));
            }
            System.out.println("Freezelist" + list.toString());
            config.setFreeze(list);
        } catch (Exception e) {
            config.setFreeze(new ArrayList<>());
        }
        return config;
    }

    public ModelConfig Conf2MC(Config config) {
        ModelConfig modelConfig = new ModelConfig();
        modelConfig.setActivation(config.getActivation());
        modelConfig.setClsType(config.getClsType());
        modelConfig.setLnType(config.getLnType());
        modelConfig.setLyrType(config.getLyrType());
        modelConfig.setPoolerType(config.getPoolerType());
        List<Integer> freezes = config.getFreeze();
        String str = "";
        if (freezes != null && freezes.size() != 0) {
            for (Integer freeze : freezes) {
                str += freeze + ",";
            }
            modelConfig.setFreeze(str.substring(0, str.length() - 1));
        }
        return modelConfig;
    }
}

class ModelParam {

    private Long modelId;

    private Long userId;

    private String modelName;

    private String basicModel;

//    private String config;

    private String description;

    private LocalDateTime updateTime;

    private String initParam;

    private String preTrainedModel;

    Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config modelConfig) {
        this.config = modelConfig;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getBasicModel() {
        return basicModel;
    }

    public void setBasicModel(String basicModel) {
        this.basicModel = basicModel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getInitParam() {
        return initParam;
    }

    public void setInitParam(String initParam) {
        this.initParam = initParam;
    }

    public String getPreTrainedModel() {
        return preTrainedModel;
    }

    public void setPreTrainedModel(String preTrainedModel) {
        this.preTrainedModel = preTrainedModel;
    }
}

class Config {
    private String lnType = "post";

    private List<Integer> freeze;

    private String clsType = "fc";

    private String lyrType = "";

    private String poolerType = "cls";

    private String activation = "gelu";

    public String getLnType() {
        return lnType;
    }

    public void setLnType(String lnType) {
        this.lnType = lnType;
    }

    public List<Integer> getFreeze() {
        return freeze;
    }

    public void setFreeze(List<Integer> freeze) {
        this.freeze = freeze;
    }

    public String getClsType() {
        return clsType;
    }

    public void setClsType(String clsType) {
        this.clsType = clsType;
    }

    public String getLyrType() {
        return lyrType;
    }

    public void setLyrType(String lyrType) {
        this.lyrType = lyrType;
    }

    public String getPoolerType() {
        return poolerType;
    }

    public void setPoolerType(String poolerType) {
        this.poolerType = poolerType;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }
}