package hku.cs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import hku.cs.entity.User;
import hku.cs.mapper.UserMapper;
import hku.cs.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hku.cs.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author xxy
 * @since 2022-05-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public User getByUsername(String username) {
        return getOne(new QueryWrapper<User>().eq("username",username));
    }
}
