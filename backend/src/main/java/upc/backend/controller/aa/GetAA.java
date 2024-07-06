package upc.backend.controller.aa;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.user.param.UpdateUserPasswordParam;
import upc.backend.entity.UserToken;
import upc.backend.util.Result;
import upc.backend.util.ResultGenerator;
import upc.backend.entity.User;
import upc.backend.service.UserA_Service;

@Controller
@CrossOrigin
@ResponseBody
@Slf4j
@RequestMapping("/api")
public class GetAA {
    @Resource
    private UserA_Service userA_Service;
    @RequestMapping(value = "/aa/bb", method = RequestMethod.GET)
    public Result bb() {
        //log.info("user token:{}", passwordParam.getToken());
        User userA = userA_Service.selectByID(3);
        if (userA != null) {
            Result result= ResultGenerator.genSuccessResult();
            result.setData(userA.getUsername());
            return result;
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.DB_ERROR.getResult());
        }
    }
}
