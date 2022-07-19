package hku.cs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hku.cs.entity.Model;
import hku.cs.entity.Task;

import java.util.List;

public interface TaskService extends IService<Task> {
    List<Task> getByuserId();

    Task getByTaskId(Long id);

    List<Task> getByName(String name, int status);

    List<Task> getRunning();

    List<Task> getComplete();
}
