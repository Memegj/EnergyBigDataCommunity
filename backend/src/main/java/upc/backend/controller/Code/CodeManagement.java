package upc.backend.controller.Code;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.BatchIdParam;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.Code.param.CodeAddParam;
import upc.backend.entity.Code;
import upc.backend.entity.UserToken;
import upc.backend.service.CodeService1;
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
public class CodeManagement {
    @Resource
    private CodeService1 codeService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/excel1/list1", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize) {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(codeService.getReferencesPage(pageUtil));
    }

    @RequestMapping(value = "/excel1", method = RequestMethod.DELETE)
//@ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam) {
        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (codeService.deleteBatch(batchIdParam.getIds())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    // "获取单条信息", "根据excelid查询"
    public Result info(@PathVariable("CodeId") Integer CodeId) {
        Code code = codeService.getReferenceById(CodeId);
        if (code == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(code);

    }

    @RequestMapping(value = "/excel1", method = RequestMethod.POST)
    public Result save(@RequestBody CodeAddParam codeAddParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                //  User user = userService.getUserDetailById(userToken.getUserid());
                Code code = new Code();
                BeanUtil.copyProperties(codeAddParam, code);
                // code.setUpload_user(user.getUsername());
                code.setUploadtime(new Date());
                Boolean result_state = codeService.add_reference(code);
                if (result_state) {
                    Result result = new ResultGenerator().genSuccessResult();
                    result.setData(true);
                    return result;
                } else return ResultGenerator.genFailResult(ServiceResultEnum.DB_ERROR.getResult());
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }
}