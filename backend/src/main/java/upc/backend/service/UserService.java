package upc.backend.service;
import jakarta.annotation.Resource;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.user.param.UserUpdateParam;
import upc.backend.entity.User;
import upc.backend.entity.UserToken;
import upc.backend.mapper.UserMapper;
import org.springframework.stereotype.Service;
import upc.backend.mapper.UserTokenMapper;
import upc.backend.util.NumberUtil;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;
import upc.backend.util.SystemUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserTokenMapper userTokenMapper;
    //用户登录
    public HashMap<String, String> login(String username, String password){
        User loginUser = userMapper.login(username, password);
        HashMap<String, String> map = new HashMap<>();
        if (loginUser != null) {
            //登录后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", loginUser.getUserid());
            UserToken userToken = userTokenMapper.selectByPrimaryKey(loginUser.getUserid());
            //当前时间
            Date now = new Date();
            //过期时间
            Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);//过期时间 48 小时
            if (userToken == null) {
                userToken = new UserToken();
                userToken.setUserid(loginUser.getUserid());
                userToken.setToken(token);
                userToken.setUpdate_time(now);
                userToken.setExpire_time(expireTime);
                //新增一条token数据
                if (userTokenMapper.insertSelective(userToken) > 0) {
                    map.put("tokenStr", token);
                    map.put("roleStr", loginUser.getUser_role());
                    return map;
                    //新增成功后返回
                    //return token;
                }
            } else {
                userToken.setToken(token);
                userToken.setUpdate_time(now);
                userToken.setExpire_time(expireTime);
                //更新
                if (userTokenMapper.updateByPrimaryKeySelective(userToken) > 0) {
                    map.put("tokenStr", token);
                    map.put("roleStr", loginUser.getUser_role());
                    return map;
                    //修改成功后返回
                    //return token;
                }
            }

        }
        map.put("tokenStr", ServiceResultEnum.LOGIN_ERROR.getResult());
        map.put("roleStr", null);
        return map;
    }
    private String getNewToken(String timeStr, Integer userid) {
        String src = timeStr + userid + NumberUtil.genRandomNum(6);
        return SystemUtil.genToken(src);
    }
    // 获取用户信息
    public User getUserDetailById(Integer userid){
        return userMapper.selectByPrimaryKey(userid);
    }

    //修改当前登录用户的密码
    public Boolean updatePassword(Integer userid, String originalPassword, String newPassword) {
        User user = userMapper.selectByPrimaryKey(userid);
        //当前用户非空才可以进行更改
        if (user != null) {
            //比较原密码是否正确
            if (originalPassword.equals(user.getPassword())) {
                //设置新密码并修改
                user.setPassword(newPassword);
                if (userMapper.updateByPrimaryKeySelective(user) > 0 && userTokenMapper.deleteByPrimaryKey(userid) > 0) {
                    //修改成功且清空当前token则返回true
                    return true;
                }
            }
        }
        return false;
    }

    //修改当前登录用户的名称信息
    public Boolean updateName(Integer userid, String username, String nickname){
        User user = userMapper.selectByPrimaryKey(userid);
        //当前用户非空才可以进行更改
        if (user != null) {
            //设置新名称并修改
            user.setUsername(username);
            user.setNickname(nickname);
            if (userMapper.updateByPrimaryKeySelective(user) > 0) {
                //修改成功则返回true
                return true;
            }
        }
        return false;
    }

    //退出
    public Boolean logout(Integer userid) {
        return userTokenMapper.deleteByPrimaryKey(userid) > 0;
    }
    //注册
    public String register(String username, String password) {
        return "ok";
    }
    //更细用户信息
    public Boolean updateUserInfo(UserUpdateParam user, Integer userid){
        return true;
    }
    public Boolean lockUsers(Integer[] ids, int lockStatus){
        if (ids.length < 1) {
            return false;
        }
        return userMapper.lockUserBatch(ids, lockStatus) > 0;
    }
    public PageResult getUsersPage(PageQueryUtil pageUtil){
        List<User> users = userMapper.findAllUserList(pageUtil);
        int total = userMapper.getNumOfTotalUsers(pageUtil);
        PageResult pageResult = new PageResult(users, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public Boolean add_User(User user){
        return userMapper.insertSelective(user) > 0;
    }
}
