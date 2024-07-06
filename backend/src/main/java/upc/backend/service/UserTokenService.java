package upc.backend.service;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.UserToken;
import upc.backend.mapper.UserMapper;
import upc.backend.mapper.UserTokenMapper;

@Service
public class UserTokenService {
    @Resource
    private UserTokenMapper userTokenMapper;

    public UserToken selectByToken(String token){
        return userTokenMapper.selectByToken(token);
    }
}
