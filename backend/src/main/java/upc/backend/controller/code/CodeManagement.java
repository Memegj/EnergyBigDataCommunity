package upc.backend.controller.code;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import upc.backend.common.BatchIdParam;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.code.Param.CodeUpdateParam;
import upc.backend.controller.collect.Param.CollectAddParam;
import upc.backend.controller.datasets.Param.DatasetsUpdateParam;
import upc.backend.controller.file.param.FileAddParam;
import upc.backend.entity.*;
import upc.backend.service.*;
import upc.backend.util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/api")
public class CodeManagement {
    @Autowired
    private StandardServletMultipartResolver standardServletMultipartResolver;
    @Resource
    private CodeService codeService;
    @Resource
    private TeamService teamService;
    @Resource
    private TeamfileService teamfileService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private CollectService collectService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/code/list", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 5) {
                    return ResultGenerator.genFailResult("参数异常！");
                }
                Map<String, Object> params = new HashMap<>(8);
                params.put("page", pageNumber);
                params.put("limit", pageSize);
                params.put("userid", userToken.getUserId());
                PageQueryUtil pageUtil = new PageQueryUtil(params);
                return ResultGenerator.genSuccessResult(codeService.getCodePage(pageUtil));
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }
    @RequestMapping(value = "/code", method = RequestMethod.DELETE)
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

    @RequestMapping(value = "/code/{codeId}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result info(@PathVariable("codeId") Integer codeId) {
        Code code = codeService.getCodeByCodeId(codeId);
        if (code == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(code);
    }

    @RequestMapping(value = "/code/username/{codeId}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result user(@PathVariable("codeId") Integer codeId) {
        Code code = codeService.getCodeByCodeId(codeId);
        User user = userService.getUserByUserId(code.getUserId());
        return ResultGenerator.genSuccessResult(user);
    }

    @RequestMapping(value = "/codedetail/{codeId}", method = RequestMethod.GET)
// "获取单条信息", "根据id查询"
    public Result info(HttpServletRequest httpServletRequest , @PathVariable("codeId") Integer codeId,
                       @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            }
            else {
                try {
                    URI requestUrl = new URI(httpServletRequest.getRequestURL().toString());
                    URI hostUrl = new URI(requestUrl.getScheme(), requestUrl.getUserInfo(), requestUrl.getHost(), requestUrl.getPort(), null, null, null);
                    Code code = codeService.getCodeByCodeId(codeId);
                    if (code == null) {
                        return ResultGenerator.genFailResult("未查询到数据");
                    }
                    // 使用 DATASETS_UPLOAD_DIC 构造文件的本地路径
                    String codeUrl = code.getUrl();
                    if (codeUrl.startsWith("/upload/code/")) {
                        codeUrl = codeUrl.substring("/upload/code/".length()); // 移除重复部分
                    }
                    String localFilePath = Constants.CODE_UPLOAD_DIC + codeUrl;
                    File file = new File(localFilePath);
                    // 获取文件大小
                    long fileSize = file.exists() ? file.length() : 0; // 确保文件存在再获取大小
                    code.setFileSize(fileSize);

                    String userName = userService.getUserNameByUserId(code.getUserId());
                    code.setUserName(userName);

                    //获取CollectId
                    Map<String, Object> params = new HashMap<>(10);
                    params.put("userid", userToken.getUserId());
                    params.put("codeid", code.getCodeId());
                    CollectQueryUtil collectUtil = new CollectQueryUtil(params);
                    Collect collect = collectService.getCollectByCodeId(collectUtil);
                    if (collect != null) {
                        code.setCollectId(collect.getCollectId());
                    } else {
                        code.setCollectId(null); // 或者可以选择其他处理方式
                    }

                    if (code == null) {
                        return ResultGenerator.genFailResult("未查询到数据");
                    }
                    Map<String, Object> result = new HashMap<>();
                    result.put("code", code);
                    result.put("hostUrl", hostUrl.toString()); // 将URI转换为字符串
                    return ResultGenerator.genSuccessResult(result);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    return ResultGenerator.genFailResult("URI解析异常");
                }
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }

    @RequestMapping(value = "/code/listmanage", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestHeader("token") String str_token,
                       @RequestParam(required = false) String searchQuery) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            }
            else {
                if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 5) {
                    return ResultGenerator.genFailResult("参数异常！");
                }
                Map<String, Object> params = new HashMap<>(10);
                params.put("page", pageNumber);
                params.put("limit", pageSize);
                params.put("userid", userToken.getUserId());
                params.put("searchQuery", searchQuery); // 将搜索查询参数加入到参数列表中
                PageQueryUtil pageUtil = new PageQueryUtil(params);
                return ResultGenerator.genSuccessResult(codeService.getCodePageByUserIdOrderByTime(pageUtil));
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }

    @RequestMapping(value = "/code/listByTeam", method = RequestMethod.GET)
    public Result listByTeam(@RequestParam(required = false) Integer pageNumber,
                             @RequestParam(required = false) Integer pageSize,
                             @RequestHeader("token") String str_token,
                             @RequestParam(required = false) String searchQuery) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            }
            else {
                if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 5) {
                    return ResultGenerator.genFailResult("参数异常！");
                }
                Map<String, Object> params = new HashMap<>(10);
                params.put("page", pageNumber);
                params.put("limit", pageSize);
                params.put("userid", userToken.getUserId());
                params.put("searchQuery", searchQuery); // 将搜索查询参数加入到参数列表中
                PageQueryUtil pageUtil = new PageQueryUtil(params);
                return ResultGenerator.genSuccessResult(codeService.getCodePageByUserIdOrderByTeamId(pageUtil));
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }

    //修改
    @RequestMapping(value = "/code", method = RequestMethod.PUT)
// @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result update(@RequestBody CodeUpdateParam codeUpdateParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                Code code = new Code();
                BeanUtil.copyProperties(codeUpdateParam, code);
                code.setUploadTime(new Date());
                if(codeService.updateCodeInfo(code)){
                    Result result = new ResultGenerator().genSuccessResult();
                    result.setData(true);
                    return result;
                } else {
                    return ResultGenerator.genFailResult("更新失败");
                }
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }

    @RequestMapping(value = "/code/teamname/{codeId}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result team(@PathVariable("codeId") Integer codeId) {
        Code code = codeService.getCodeByCodeId(codeId);
        Team team = teamService.getTeamByTeamId(code.getTeamId());
        return ResultGenerator.genSuccessResult(team);
    }

    @RequestMapping(value = "/code/detail", method = RequestMethod.PUT)
// @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result updatedetail(@RequestBody CodeUpdateParam codeUpdateParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                Code code = new Code();
                BeanUtil.copyProperties(codeUpdateParam, code);
                if(codeService.updateCodeInfo(code)){
                    Result result = new ResultGenerator().genSuccessResult();
                    result.setData(true);
                    return result;
                } else {
                    return ResultGenerator.genFailResult("更新失败");
                }
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }

    @RequestMapping(value = "/code/collect", method = RequestMethod.POST)
    public Result save(@RequestBody CollectAddParam collectAddParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                if (collectAddParam.getCodeId()==null ) {
                    return ResultGenerator.genFailResult("参数异常！");
                }
                else {
                    Collect collect = new Collect();
                    collect.setCodeId(collectAddParam.getCodeId());
                    collect.setUserId(userToken.getUserId());
                    collect.setCollectType("代码集");
                    if (collectService.add_collect(collect)) {
                        Result result = new ResultGenerator().genSuccessResult();
                        result.setData(true);
                        return result;
                    }
                    else return ResultGenerator.genFailResult("加入收藏失败");
                }
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }

    @RequestMapping(value = "/code/collect", method = RequestMethod.DELETE)
    //@ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result deletecollect(@RequestBody BatchIdParam batchIdParam) {

        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (collectService.deleteBatch(batchIdParam.getIds())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
}
