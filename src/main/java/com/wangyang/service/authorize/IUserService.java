package com.wangyang.service.authorize;


import com.wangyang.pojo.authorize.User;
import com.wangyang.pojo.authorize.UserDetailDTO;
import com.wangyang.pojo.authorize.UserParam;
import com.wangyang.pojo.dto.UserDto;
import com.wangyang.service.base.IAuthorizeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/5/5
 */
public interface IUserService  extends IAuthorizeService<User> {
    User addUser(User user);

    User addUser(UserParam userParam);
    User updateUser(int id, UserParam userParam);
    User updateUser(int id, UserParam userParam, MultipartFile multipartFile);
    List<UserDto> listAllUserDto();
    User delUser(int id);
    User findUserById(int id);
    List<User> findAllById(Collection<Integer> id);
    Page<User> pageUser(Pageable pageable);
    UserDetailDTO login(String username, String password);
    User findUserByUsername(String username);

    UserDto findUserDaoById(int userId);
    // ---------------------------------------
}
