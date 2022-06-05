package hku.cs.mapper;

import hku.cs.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author xxy
 * @since 2022-05-23
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

}
