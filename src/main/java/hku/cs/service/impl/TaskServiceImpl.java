package hku.cs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hku.cs.entity.Model;
import hku.cs.entity.Task;
import hku.cs.entity.User;
import hku.cs.mapper.TaskMapper;
import hku.cs.mapper.UserMapper;
import hku.cs.service.TaskService;
import hku.cs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @Override
    public List<Task> getByuserId() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        System.out.println("user_id " + user.getId());
        return this.list(new QueryWrapper<Task>().eq("user_id", user.getId()));
    }

    @Override
    public Task getByTaskId(Long id) {
        return this.getOne(new QueryWrapper<Task>().eq("task_id", id));
    }

    @Override
    public List<Task> getByName(String name) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        System.out.println("user_id " + user.getId());
        return this.list(new QueryWrapper<Task>().eq("user_id", user.getId()).like("task_name", name));
    }

    @Override
    public List<Task> getRunning() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        System.out.println("user_id " + user.getId());
        return this.list(new QueryWrapper<Task>().eq("user_id", user.getId()).like("status", 1));
    }

    @Override
    public List<Task> getComplete() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        System.out.println("user_id " + user.getId());
        return this.list(new QueryWrapper<Task>().eq("user_id", user.getId()).eq("status", 2));
    }

}
