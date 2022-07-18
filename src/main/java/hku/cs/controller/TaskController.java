package hku.cs.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import hku.cs.common.lang.Result;
import hku.cs.entity.Model;
import hku.cs.entity.Task;
import hku.cs.entity.TaskDetail;
import hku.cs.entity.User;
import hku.cs.service.TaskService;
import hku.cs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1.0/task")
@CrossOrigin
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    UserService userService;
    private final static SimpleDateFormat yyMMdd = new SimpleDateFormat("yyMMdd");

    @GetMapping("/list")
    public Result list() {
        List<Task> tasks = taskService.getByuserId();
        return Result.succ(tasks);
    }

    @DeleteMapping("/del/{task_id}")
    public Result del(@PathVariable Long task_id) {
        taskService.removeById(task_id);
        return Result.succ(task_id);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Task task) {
        System.out.println("model:" + task.getModelId());
        System.out.println(task.getDatasetIdTrain());
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String fiveNumber = timeMillis.substring(timeMillis.length() - 8);
        String date = yyMMdd.format(new Date());
        task.setTaskId(Long.parseLong(date + fiveNumber));
        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long userId = user.getId();
        task.setUserId(userId);
        task.setStatus(0);
        taskService.save(task);
        return Result.succ(task);
    }

    @GetMapping("/detail")
    public Result get(@RequestParam Long task_id) throws IOException {
        Task task = taskService.getById(task_id);
        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long user_id = user.getId();

        long nowSecond = task.getStartTime().toEpochSecond(ZoneOffset.ofHours(0));
        long endSecond = task.getEndTime().toEpochSecond(ZoneOffset.ofHours(0));
        long absSeconds = Math.abs(nowSecond - endSecond);

        System.out.println(nowSecond + "||" + endSecond);
        long s = absSeconds % 60;
        long m = absSeconds / 60 % 60;
        long h = absSeconds / 60 / 60 % 24;
//        long d = absSeconds / 60 / 60 / 24;

        TaskDetail taskDetail = new TaskDetail();
        String duration = h + "H" + m + "M" + s + "S";

        taskDetail.setTrainingTime(duration);
        //...
        String path = "/var/doc/usr" + user_id + "/task/" + task_id + "/eval_results.json";
        File file = new File(path);
        String jsonData = getStr(file);

        JSONObject parse = (JSONObject) JSONObject.parse(jsonData);
        System.out.println(parse.toJSONString());
        return Result.succ(
                MapUtil.builder()
                        .put("detail", parse)
                        .map()
        );
    }

    @GetMapping("/getByName")
    public Result getByName(@RequestParam String name) {
        List<Task> list = taskService.getByName(name);
        return Result.succ(list);
    }

    @GetMapping("/getRunning")
    public Result getRunning() {
        List<Task> list = taskService.getRunning();
        return Result.succ(list);
    }

    @GetMapping("/getComplete")
    public Result getComplete() {
        List<Task> list = taskService.getComplete();
        return Result.succ(list);
    }

    public String getStr(File jsonFile) {
        String jsonStr = "";
        try {
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
