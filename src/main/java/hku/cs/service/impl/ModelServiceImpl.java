package hku.cs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hku.cs.entity.Model;
import hku.cs.entity.User;
import hku.cs.mapper.ModelMapper;
import hku.cs.mapper.UserMapper;
import hku.cs.service.ModelService;
import hku.cs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.List;

@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @Override
    public List<Model> getByuserId() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        return this.list(new QueryWrapper<Model>().eq("userid",user.getId()));
    }
}
