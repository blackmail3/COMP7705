package hku.cs.controller;


import hku.cs.common.lang.Result;
import hku.cs.entity.Model;
import hku.cs.entity.Task;
import hku.cs.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping("list")
    public Result list(){
        List<Task> tasks = taskService.getByuserId();
        return Result.succ(tasks);
    }

    @PostMapping("add")
    public Result add(@RequestBody Task task){
        taskService.save(task);
        return Result.succ(task);
    }

}
