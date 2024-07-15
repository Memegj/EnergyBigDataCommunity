package upc.backend.controller.literature;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import upc.backend.common.BatchIdParam;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.collect.Param.CollectAddParam;
import upc.backend.controller.datasets.Param.DatasetsUpdateParam;
import upc.backend.controller.literature.Param.LiteratureUpdateParam;
import upc.backend.entity.*;
import upc.backend.service.*;
import upc.backend.util.*;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class LiteratureManagement {
    @Autowired
    private StandardServletMultipartResolver standardServletMultipartResolver;
    @Resource
    private LiteratureService literatureService;
    @Resource
    private CollectService collectService;
    @Resource
    private TeamService teamService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/literature/list", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestParam(required = false) String LiterType,
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
                params.put("litertype", LiterType); // 添加 LiterType 到参数中
                PageQueryUtil pageUtil = new PageQueryUtil(params);
                return ResultGenerator.genSuccessResult(literatureService.getLiteraturePage(pageUtil));
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }

    @RequestMapping(value = "/literature/listmanage", method = RequestMethod.GET)
    public Result managelist(@RequestParam(required = false) Integer pageNumber,
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
                return ResultGenerator.genSuccessResult(literatureService.getLiteraturePageByUserIdOrderByTime(pageUtil));
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }

    @RequestMapping(value = "/literature/listByTeam", method = RequestMethod.GET)
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
                return ResultGenerator.genSuccessResult(literatureService.getLiteraturePageByUserIdOrderByTeamId(pageUtil));
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }


    @RequestMapping(value = "/literature/collect", method = RequestMethod.DELETE)
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
    @RequestMapping(value = "/literature", method = RequestMethod.DELETE)
    //@ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam) {

        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        collectService.deleteBatchByLiterId(batchIdParam.getIds());
        if (literatureService.deleteBatch(batchIdParam.getIds())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    @RequestMapping(value = "/literature/{literId}", method = RequestMethod.GET)
// "获取单条信息", "根据id查询"
    public Result info(HttpServletRequest httpServletRequest , @PathVariable("literId") Integer literId,
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
                    Literature literature = literatureService.getLiteratureByLiterId(literId);
                    if (literature == null) {
                        return ResultGenerator.genFailResult("未查询到数据");
                    }
                    // 使用 DATASETS_UPLOAD_DIC 构造文件的本地路径
                    String literatureUrl = literature.getUrl();
                    if (literatureUrl.startsWith("/upload/literature/")) {
                        literatureUrl = literatureUrl.substring("/upload/literature/".length()); // 移除重复部分
                    }
                    String localFilePath = Constants.LITERATURE_UPLOAD_DIC + literatureUrl;
                    File file = new File(localFilePath);
                    // 获取文件大小
                    long fileSize = file.exists() ? file.length() : 0; // 确保文件存在再获取大小
                    literature.setFileSize(fileSize);

                    String userName = userService.getUserNameByUserId(literature.getUserId());
                    literature.setUserName(userName);

                    //获取CollectId
                    Map<String, Object> params = new HashMap<>(10);
                    params.put("userid", userToken.getUserId());
                    params.put("literid", literature.getLiterId());
                    CollectQueryUtil collectUtil = new CollectQueryUtil(params);
                    Collect collect = collectService.getCollectByLiterId(collectUtil);
                    if (collect != null) {
                        literature.setCollectId(collect.getCollectId());
                    } else {
                        literature.setCollectId(null); // 或者可以选择其他处理方式
                    }

                    if (literature == null) {
                        return ResultGenerator.genFailResult("未查询到数据");
                    }
                    Map<String, Object> result = new HashMap<>();
                    result.put("literature", literature);
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

    @RequestMapping(value = "/literature/teamname/{literId}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result team(@PathVariable("literId") Integer literId) {
        Literature literature = literatureService.getLiteratureByLiterId(literId);
        Team team = teamService.getTeamByTeamId(literature.getTeamId());
        return ResultGenerator.genSuccessResult(team);
    }

    //修改
    @RequestMapping(value = "/literature", method = RequestMethod.PUT)
// @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result update(@RequestBody LiteratureUpdateParam literatureUpdateParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                Literature literature = new Literature();
                BeanUtil.copyProperties(literatureUpdateParam, literature);
                literature.setUploadTime(new Date());
                if(literatureService.updateLiteratureInfo(literature)){
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

    @RequestMapping(value = "/literature/detail", method = RequestMethod.PUT)
// @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result updatedetail(@RequestBody LiteratureUpdateParam literatureUpdateParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                Literature literature = new Literature();
                BeanUtil.copyProperties(literatureUpdateParam, literature);
                if(literatureService.updateLiteratureInfo(literature)){
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

    @RequestMapping(value = "/literature/collect", method = RequestMethod.POST)
    public Result save(@RequestBody CollectAddParam collectAddParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                if (collectAddParam.getLiterId()==null ) {
                    return ResultGenerator.genFailResult("参数异常！");
                }
                else {
                    Collect collect = new Collect();
                    collect.setLiterId(collectAddParam.getLiterId());
                    collect.setUserId(userToken.getUserId());
                    collect.setCollectType("文献资料");
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


}
