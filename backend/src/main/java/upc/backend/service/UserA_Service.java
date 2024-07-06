package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.User;
import upc.backend.mapper.UserAMapper;

@Service
public class UserA_Service {
    @Resource
    private UserAMapper userAMapper;
    public User selectByID(Integer userid){
        return userAMapper.selectByID(userid);
    }
}
