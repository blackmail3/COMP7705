package hku.cs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hku.cs.entity.ModelConfig;

public interface ModelConfigService extends IService<ModelConfig> {
    ModelConfig getByModelId(Long ModelId);
    boolean updateByModelId(Long model_id, ModelConfig modelConfig);
}
