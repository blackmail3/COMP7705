package hku.cs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hku.cs.entity.Model;
import hku.cs.entity.ModelConfig;
import hku.cs.mapper.ModelConfigMapper;
import hku.cs.service.ModelConfigService;
import org.springframework.stereotype.Service;

@Service
public class ModelConfigServiceImpl extends ServiceImpl<ModelConfigMapper, ModelConfig> implements ModelConfigService {
    @Override
    public ModelConfig getByModelId(Long ModelId) {
        return this.getOne(new QueryWrapper<ModelConfig>().eq("model_id", ModelId));
    }

    @Override
    public boolean updateByModelId(Long model_id, ModelConfig modelConfig) {
        return this.update(modelConfig, new QueryWrapper<ModelConfig>().eq("model_id", model_id));
    }
}
