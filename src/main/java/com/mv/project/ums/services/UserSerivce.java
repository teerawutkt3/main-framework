package com.mv.project.ums.services;

import com.google.common.collect.Lists;
import com.mv.project.ums.entities.Role;
import com.mv.project.ums.entities.User;
import com.mv.project.ums.entities.UserRole;
import com.mv.project.ums.repositories.RoleRepository;
import com.mv.project.ums.repositories.UserRepository;
import com.mv.project.ums.repositories.UserRoleRepository;
import com.mv.project.ums.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserSerivce {
    private static final Logger logger = LoggerFactory.getLogger(UserSerivce.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public UserVo save(UserVo userVo) {
        User user = userVo.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user = userRepository.save(user);
        //==> Assign roles
        userRoleRepository.deleteByUserId(user.getId());
        List<UserRole> userRoles = new ArrayList<>();
        for (long roleId: userVo.getRoleIds()) {
            Optional<Role> roleOptional = roleRepository.findById(roleId);
            if(roleOptional.isPresent()){
                UserRole userrole = new UserRole();
                userrole.setUser(user);
                userrole.setRole(roleOptional.get());
                userRoles.add(userrole);
            }
            userRoleRepository.saveAll(userRoles);
            userVo.setUser(user);
        }
        return userVo;
    }

    public User findByUserId(long userId){
        return userRepository.findById(userId).get();
    }

    public List<User> getUserAll() {
        List<User> users = userRepository.findAllByOrderByCreatedDateDesc();
        ArrayList<User> userList = new ArrayList<User>();
        if (!users.toString().isEmpty())
            userList = Lists.newArrayList(users);
        return userList;
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteUser(Long userId) {
        userRoleRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
        logger.info("Method deleteUser id: {} success", userId);
    }
}
