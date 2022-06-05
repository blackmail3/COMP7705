package hku.cs.service;

import hku.cs.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author xxy
 * @since 2022-05-23
 */
public interface UserService extends IService<User> {
    User getByUsername(String username);

//
//    String getUserAuthorityInfo(Long userId);
//
//    void clearUserAuthorityInfo(String username);
//
//    void clearUserAuthorityInfoByRoleId(Long roleId);
//
//    void clearUserAuthorityInfoByMenuId(Long menuId);
}
