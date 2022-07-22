package hku.cs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hku.cs.entity.Dataset;
import hku.cs.entity.User;
import hku.cs.mapper.DatasetMapper;
import hku.cs.service.DatasetService;
import hku.cs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatasetServiceImpl extends ServiceImpl<DatasetMapper, Dataset> implements DatasetService {
    @Autowired
    UserService userService;

    @Override
    public List<Dataset> getByuserId() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        System.out.println(user.toString());
        System.out.println(user.getId());
        return this.list(new QueryWrapper<Dataset>().eq("user_id", user.getId()));
    }

    @Override
    public Dataset getByDatasetId(Long id) {
        return this.getOne(new QueryWrapper<Dataset>().eq("dataset_id", id));
    }

    @Override
    public List<Dataset> getByName(String dataset_name) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        System.out.println(user.toString());
        return this.list(new QueryWrapper<Dataset>().eq("user_id", user.getId()).like("name", dataset_name));
    }
}
