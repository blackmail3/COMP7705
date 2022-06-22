package hku.cs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hku.cs.entity.Model;
import hku.cs.entity.Task;

import java.util.List;

public interface TaskService extends IService<Task> {
    List<Task> getByuserId();
    Task getByTaskId(Long id);
//    Long getDatasetId();
//    Long getModelId();
}
