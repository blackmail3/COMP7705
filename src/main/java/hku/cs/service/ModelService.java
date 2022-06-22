package hku.cs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hku.cs.entity.Model;
import hku.cs.entity.ModelConfig;

import java.util.List;

public interface ModelService extends IService<Model> {
    List<Model> getByuserId();
    ModelConfig getModelConfig(Long model_id);
}
