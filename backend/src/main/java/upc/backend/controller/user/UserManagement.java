package upc.backend.controller.user;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.BatchIdParam;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.user.param.UpdateUserPasswordParam;
import upc.backend.controller.user.param.UserAddParam;
import upc.backend.controller.user.param.UserUpdateParam;
import upc.backend.entity.User;
import upc.backend.entity.UserToken;
import upc.backend.service.UserService;
import upc.backend.service.UserTokenService;
import upc.backend.util.BeanUtil;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.Result;
import upc.backend.util.ResultGenerator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserManagement {
    private static final Logger logger = LoggerFactory.getLogger(UserManagement.class);
    @Resource
    private UserService userService;
    @Resource
    private UserTokenService userTokenService;

    @RequestMapping(value = "/users/list", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestParam(required = false) Integer lockStatus) {
        //, @RequestHeader("token") String token
        // logger.info("this test tokon:{}", token);
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        if (lockStatus != null) {
            params.put("userStatus", lockStatus);
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(userService.getUsersPage(pageUtil));
    }

    //用户禁用与解除禁用(0-未锁定 1-已锁定)
    @RequestMapping(value = "/users/{lockStatus}", method = RequestMethod.PUT)
    public Result lockUser(@RequestBody BatchIdParam batchIdParam, @PathVariable int lockStatus) {
        //logger.info("user:{}", user.toString());
        if (batchIdParam==null||batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (lockStatus != 0 && lockStatus != 1) {
            return ResultGenerator.genFailResult("操作非法！");
        }
        if (userService.lockUsers(batchIdParam.getIds(), lockStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("禁用失败");
        }
    }
    @RequestMapping(value = "/users/user_add", method = RequestMethod.POST)
    public Result user_add(@RequestBody UserAddParam userAddParam) {
        logger.info("UserName:{}", userAddParam.getUserName());
        if (userAddParam.getUserName()==null || userAddParam.getNickName()==null || userAddParam.getUserPassword()==null) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        else {
            User user = new User();
            user.setUserId(userAddParam.getUserId());
            user.setUserName(userAddParam.getUserName());
            user.setNickName(userAddParam.getNickName());
            user.setUserEmail(userAddParam.getUserEmail());
            user.setUserCollege(userAddParam.getUserCollege());
            user.setUserPassword(userAddParam.getUserPassword());
            user.setUser_role(userAddParam.getUser_role());
            user.setUserLocked((byte) 0);
            user.setRegisterTime(new Date());
            if (userService.add_User(user)) {
                Result result = new ResultGenerator().genSuccessResult();
                result.setData(true);
                return result;
            }
            else return ResultGenerator.genFailResult("用户新增失败");
        }
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result info(@PathVariable("userId") Integer UserId) {
        User user = userService.getUserDetailById(UserId);
        if (user == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(user);
    }
    //修改
    @RequestMapping(value = "/users/update", method = RequestMethod.PUT)
    // @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result update(@RequestBody UserUpdateParam userUpdateParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                User user = new User();
                BeanUtil.copyProperties(userUpdateParam, user);
                userService.updateUserInfo(user);
                Result result = new ResultGenerator().genSuccessResult();
                result.setData(true);
                return result;

            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }
    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    //@ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam) {

        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (userService.deleteBatch(batchIdParam.getIds())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    @RequestMapping(value = "/users/password", method= RequestMethod.PUT)
    // @ApiOperation(value = "重置用户密码", notes = "将指定用户的密码重置为'123456'")
    public Result resetPassword(@RequestBody UserUpdateParam userUpdateParam) {
        if (userUpdateParam == null ) {
            return ResultGenerator.genFailResult("参数异常！");
        }

        if(userService.resetPassword(userUpdateParam.getUserId(), userUpdateParam.getUserPassword())){
            return ResultGenerator.genSuccessResult("密码重置成功！");
        } else {
            return ResultGenerator.genFailResult("密码重置失败，请稍后再试。");
        }
    }
}
