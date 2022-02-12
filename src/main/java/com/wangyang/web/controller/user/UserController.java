package com.wangyang.web.controller.user;

import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.pojo.authorize.*;
import com.wangyang.pojo.dto.UserDto;
import com.wangyang.pojo.support.Token;
import com.wangyang.service.authorize.IRoleService;
import com.wangyang.service.authorize.IUserService;
import com.wangyang.util.TokenProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/5/5
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    IUserService userService;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    IRoleService roleService;

    @GetMapping
    public Page<User> page(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return userService.pageUser(pageable);
    }

    @GetMapping("/listAll")
    public List<UserDto> listAll(){
        return userService.listAllUserDto();
    }



    @PostMapping("/login")
    @Anonymous
    public LoginUser login(@RequestBody UserLoginParam inputUser){
        UserDetailDTO user = userService.login(inputUser.getUsername(), inputUser.getPassword());
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(user,loginUser);
        Token token = tokenProvider.generateToken(user);
        loginUser.setToken(token.getToken());
        return loginUser;
    }

    @GetMapping("/getCurrent")
    public User getCurrentUser(HttpServletRequest request){
        Object obj = request.getAttribute("user");
        if(obj!=null){
            User currUser = (User) obj;
            int userId = currUser.getId();
            User user = findById(userId);
            return user;
        }
        return null;
    }

    @PostMapping
    public User addUser(@RequestBody @Validated UserParam inputUser){
        return userService.addUser(inputUser);
    }

//    @PostMapping(value = "/update/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public User updateUser(@PathVariable("id") Integer id, UserParam user,@RequestPart(value = "file",required = false) MultipartFile file){
//        return userDetailService.updateUser(id,user,file);
//    };
    @PostMapping(value = "/update/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public User updateUser(@PathVariable("id") Integer id,
                           UserParam user,
                           @RequestPart(value = "file",required = false) MultipartFile file){
        return userService.updateUser(id,user,file);
    }

    @GetMapping("/del/{id}")
    public User delUser(@PathVariable("id") Integer id){
        return userService.delUser(id);
    }
    @GetMapping("/findById/{id}")
    public User findById(@PathVariable("id") Integer id){
        User user = userService.findById(id);
        user.setPassword(null);
        return user;
    }
}
