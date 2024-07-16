package upc.backend.service;
import jakarta.annotation.Resource;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.user.param.UserUpdateParam;
import upc.backend.entity.User;
import upc.backend.entity.UserToken;
import org.springframework.stereotype.Service;
import upc.backend.mapper.UserMapper;
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
    public HashMap<String, String> login(Integer UserId, String UserPassword){
        User loginUser = userMapper.login(UserId, UserPassword);
        HashMap<String, String> map = new HashMap<>();
        if (loginUser != null) {
            //登录后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", loginUser.getUserId());
            UserToken userToken = userTokenMapper.selectByPrimaryKey(loginUser.getUserId());
            //当前时间
            Date now = new Date();
            //过期时间
            Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);//过期时间 48 小时
            if (userToken == null) {
                userToken = new UserToken();
                userToken.setUserId(loginUser.getUserId());
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
    private String getNewToken(String timeStr, Integer UserId) {
        String src = timeStr + UserId + NumberUtil.genRandomNum(6);
        return SystemUtil.genToken(src);
    }
    // 获取用户信息
    public User getUserDetailById(Integer UserId){
        return userMapper.selectByPrimaryKey(UserId);
    }

    public String getUserNameByUserId(Integer UserId){
        return userMapper.getUserNameByUserId(UserId);
    }
    //修改当前登录用户的密码
    public Boolean updatePassword(Integer UserId, String originalPassword, String newPassword) {
        User user = userMapper.selectByPrimaryKey(UserId);
        //当前用户非空才可以进行更改
        if (user != null) {
            //比较原密码是否正确
            if (originalPassword.equals(user.getUserPassword())) {
                //设置新密码并修改
                user.setUserPassword(newPassword);
                if (userMapper.updateByPrimaryKeySelective(user) > 0 && userTokenMapper.deleteByPrimaryKey(UserId) > 0) {
                    //修改成功且清空当前token则返回true
                    return true;
                }
            }
        }
        return false;
    }

  public Boolean resetPassword(Integer UserId, String UserPassword) {


        return userMapper.resetPassword(UserId,UserPassword) > 0;}



//修改当前登录用户的名称信息
    public Boolean updateName(Integer UserId, String NickName,String UerEmail, String UerCollege){
        User user = userMapper.selectByPrimaryKey(UserId);
        //当前用户非空才可以进行更改
        if (user != null) {
            //设置新名称并修改
            user.setNickName(NickName);
            user.setUserEmail(UerEmail);
            user.setUserCollege(UerCollege);
            if (userMapper.updateByPrimaryKeySelective(user) > 0) {
                //修改成功则返回true
                return true;
            }
        }
        return false;
    }

    //退出
    public Boolean logout(Integer UserId) {
        return userTokenMapper.deleteByPrimaryKey(UserId) > 0;
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
    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return userMapper.deleteBatch(ids) > 0;
    }

    //更新用户信息
    public Boolean updateUserInfo(User user){
        return userMapper.updateByPrimaryKeySelective(user) > 0;
    }



    public User getUserByUserId(Integer UserId){
        return userMapper.selectByUserId(UserId);
    }
}

