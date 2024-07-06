package upc.backend.controller.user;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.BatchIdParam;
import upc.backend.controller.user.param.UserAddParam;
import upc.backend.entity.User;
import upc.backend.service.UserService;
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

    @RequestMapping(value = "/users", method = RequestMethod.GET)
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
        logger.info("username:{}", userAddParam.getUsername());
        if (userAddParam.getUsername()==null || userAddParam.getNickname()==null || userAddParam.getPassword()==null) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        else {
            User user = new User();
            user.setUsername(userAddParam.getUsername());
            user.setNickname(userAddParam.getNickname());
            user.setPassword(userAddParam.getPassword());
            user.setUser_role(userAddParam.getUser_role());
            user.setLocked((byte) 0);
            user.setDeleted((byte) 0);
            user.setRegister_time(new Date());
            if (userService.add_User(user)) {
                Result result = new ResultGenerator().genSuccessResult();
                result.setData(true);
                return result;
            }
            else return ResultGenerator.genFailResult("用户新增失败");
        }
    }
}