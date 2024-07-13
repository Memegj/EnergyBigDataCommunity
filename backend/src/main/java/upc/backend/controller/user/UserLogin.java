package upc.backend.controller.user;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.user.annotation.TokenToUser;
import upc.backend.controller.user.param.UserLoginParam;
import upc.backend.controller.user.param.UpdateUserNameParam;
import upc.backend.controller.user.param.UpdateUserPasswordParam;
import upc.backend.entity.User;
import upc.backend.entity.UserToken;
import upc.backend.service.UserService;
import upc.backend.service.UserTokenService;
import upc.backend.util.Result;
import upc.backend.util.ResultGenerator;

import java.util.HashMap;

@CrossOrigin
@ResponseBody
@Slf4j
@Controller
@RequestMapping("/api")
public class UserLogin {
    @Resource
    private UserService userService;
    @Resource
    private UserTokenService userTokenService;

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public Result<String> login(@RequestBody UserLoginParam loginParam) {
        HashMap<String, String> loginResult = userService.login(loginParam.getUserid(), loginParam.getPassword());
        //log.info("manage login api,username={},loginResult={}", loginParam.getUsername(), loginResult);
        //登录成功
        if (StringUtils.hasText(loginResult.get("tokenStr")) && loginResult.get("tokenStr").length() == Constants.TOKEN_LENGTH) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        //登录失败
        return ResultGenerator.genFailResult(loginResult.get("tokenStr"));
    }
    @RequestMapping(value = "/user/profile", method = RequestMethod.GET)
    public Result profile(@RequestHeader("token") String str_token) {
        //NativeWebRequest webRequest = null;
        //String str_token = webRequest.getHeader("token");
        //str_token = str_token.replaceAll("\"","");
        //log.info("user's token:{}", str_token);
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            }
            else {
                User user = userService.getUserDetailById(userToken.getUserId());
                Result result = ResultGenerator.genSuccessResult();
                result.setData(user);
                return result;
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
//        User userEntity = userService.getUserDetailById(user.getUserid());
//        if (userEntity != null) {
//            userEntity.setPassword("******");
//            Result result = ResultGenerator.genSuccessResult();
//            result.setData(userEntity);
//            return result;
//        }
//        return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
    }

    @RequestMapping(value = "/user/password", method = RequestMethod.PUT)
    public Result passwordUpdate(@RequestBody UpdateUserPasswordParam passwordParam) {
        //log.info("user token:{}", passwordParam.getToken());
        UserToken userToken = userTokenService.selectByToken(passwordParam.getToken());
        if (userService.updatePassword(userToken.getUserId(), passwordParam.getOriginalPassword(), passwordParam.getNewPassword())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.DB_ERROR.getResult());
        }
    }

    @RequestMapping(value = "/user/name", method = RequestMethod.PUT)
    public Result nameUpdate(@RequestBody UpdateUserNameParam nameParam) {
        //log.info("user:{}", nameParam.getUsername());
        UserToken userToken = userTokenService.selectByToken(nameParam.getToken());
        if (userService.updateName(userToken.getUserId(), nameParam.getUsername(), nameParam.getNickname())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.DB_ERROR.getResult());
        }
    }

    @RequestMapping(value = "/user/logout", method = RequestMethod.DELETE)
    public Result logout(@TokenToUser UserToken user) {
        //log.info("user:{}", user.toString());
        userService.logout(user.getUserId());
        return ResultGenerator.genSuccessResult();
    }
}
