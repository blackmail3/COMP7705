package hku.cs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hku.cs.entity.Dataset;

import java.util.List;

public interface DatasetService extends IService<Dataset> {
    List<Dataset> getByuserId();
    Dataset getByDatasetId(Long id);
    List<Dataset> getByName(String dataset_name);
}
