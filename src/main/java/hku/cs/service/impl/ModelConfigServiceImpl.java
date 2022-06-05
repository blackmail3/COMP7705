package hku.cs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hku.cs.entity.Model;
import hku.cs.entity.ModelConfig;
import hku.cs.mapper.ModelConfigMapper;
import hku.cs.service.ModelConfigService;
import org.springframework.stereotype.Service;

@Service
public class ModelConfigServiceImpl extends ServiceImpl<ModelConfigMapper, ModelConfig> implements ModelConfigService {

}
