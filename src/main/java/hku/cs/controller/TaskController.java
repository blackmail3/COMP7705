package hku.cs.controller;


import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import hku.cs.common.lang.Result;
import hku.cs.entity.Model;
import hku.cs.entity.Task;
import hku.cs.entity.TaskDetail;
import hku.cs.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.io.*;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping("/list")
    public Result list(){
        List<Task> tasks = taskService.getByuserId();
        return Result.succ(tasks);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Task task){
        taskService.save(task);
        return Result.succ(task);
    }

    @GetMapping("/detail")
    public Result get(@RequestParam Long taskId) throws IOException {
        Task task = taskService.getById(taskId);

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
        String path = "/";
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String training_output = "";
        StringBuffer sb = new StringBuffer();
        while((training_output = br.readLine()) != null){
            sb.append(training_output);
        }
        br.close();

        System.out.println(sb);
//        taskDetail.setAccuracy();

        System.out.println();
        return Result.succ(
            taskDetail
        );
    }

}
