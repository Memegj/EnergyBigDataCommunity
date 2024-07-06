package upc.backend.controller.references;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.common.BatchIdParam;
import upc.backend.controller.references.param.ReferenceAddParam;
import upc.backend.controller.references.param.ReferenceUpdateParam;
import upc.backend.entity.*;
import upc.backend.service.ReferenceService;
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
public class ReferenceManagement {
    @Resource
    private ReferenceService referenceService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/references/list", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize) {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(referenceService.getReferencesPage(pageUtil));
    }

    @RequestMapping(value = "/references", method = RequestMethod.DELETE)
    //@ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam) {

        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (referenceService.deleteBatch(batchIdParam.getIds())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    @RequestMapping(value = "/references/{id}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result info(@PathVariable("id") Integer id) {
        Reference reference = referenceService.getReferenceById(id);
        if (reference == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(reference);
    }

    // 新增文献
    @RequestMapping(value = "/references", method = RequestMethod.POST)
    public Result save(@RequestBody ReferenceAddParam referenceAddParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            }
            else {
                User user = userService.getUserDetailById(userToken.getUserid());
                Reference reference = new Reference();
                BeanUtil.copyProperties(referenceAddParam, reference);
                reference.setUpload_user(user.getUsername());
                reference.setUpload_time(new Date());
                Boolean result_state = referenceService.add_reference(reference);
                if (result_state) {
                    Result result = new ResultGenerator().genSuccessResult();
                    result.setData(true);
                    return result;
                }
                else return ResultGenerator.genFailResult(ServiceResultEnum.DB_ERROR.getResult());
            }
        }
        else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }
    //修改
    @RequestMapping(value = "/references", method = RequestMethod.PUT)
    // @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result update(@RequestBody ReferenceUpdateParam referenceUpdateParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            }
            else {
                User user = userService.getUserDetailById(userToken.getUserid());
                Reference reference = new Reference();
                BeanUtil.copyProperties(referenceUpdateParam, reference);
                reference.setUpload_user(user.getUsername());
                reference.setUpload_time(new Date());
                referenceService.updateReferenceInfo(reference);
                Result result = new ResultGenerator().genSuccessResult();
                result.setData(true);
                return result;
            }
        }
        else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }
}
