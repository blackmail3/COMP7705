package hku.cs.controller;

import cn.hutool.core.map.MapUtil;
import hku.cs.common.lang.Result;
import hku.cs.entity.Dataset;
import hku.cs.entity.User;
import hku.cs.service.DatasetService;
import hku.cs.service.UserService;
import hku.cs.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/dataset")
@CrossOrigin
public class DatasetController {
    @Autowired
    UserService userService;
    @Autowired
    DatasetService datasetService;
    @Autowired
    RedisUtil redisUtil;

    private final static SimpleDateFormat yyMMdd = new SimpleDateFormat("yyMMdd");

    @PostMapping("/add")
    public Result add(@RequestBody Dataset dataset) {
        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long userId = user.getId();
        dataset.setUserId(userId);
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String fiveNumber = timeMillis.substring(timeMillis.length() - 8);
        String date = yyMMdd.format(new Date());
        Object ob = redisUtil.get("datasetPath_" + userId);
        System.out.println(ob.toString());
        dataset.setPath(ob.toString());
        dataset.setDatasetId(Long.parseLong(date + fiveNumber));
        dataset.setStatus(1);
        dataset.setUpdateTime(LocalDateTime.now());
        datasetService.save(dataset);
        System.out.println(dataset.toString());
        return Result.succ(dataset);
    }

    @GetMapping("/get")
    public Result getByIdName(@RequestParam @Nullable String dataset_id, @RequestParam @Nullable String name) {
        System.out.println("GET dataset, ID" + dataset_id + ", NAME" + name);
        if (dataset_id != null && !dataset_id.equals("")) {
            Dataset dataset = datasetService.getById(Long.parseLong(dataset_id));
            return Result.succ(dataset);
        } else {
            System.out.println(datasetService.getByName(name).toString());
            List<Dataset> list = new ArrayList<>(datasetService.getByName(name));
            return Result.succ(list);
        }

    }

    @PutMapping("/edit")
    public Result edit(@RequestBody Dataset dataset) {
        dataset.setUpdateTime(LocalDateTime.now());
        boolean res = datasetService.updateById(dataset);
        return Result.succ(
                MapUtil.builder()
                        .put("res", res)
                        .put("dataset", dataset)
                        .map()
        );
    }

    @DeleteMapping("/del/{dataset_id}")
    public Result del(@PathVariable Long dataset_id) {
        datasetService.removeById(dataset_id);
        return Result.succ(dataset_id);
    }
}
