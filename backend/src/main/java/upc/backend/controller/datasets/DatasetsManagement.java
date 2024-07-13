package upc.backend.controller.datasets;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import upc.backend.common.BatchIdParam;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.aa.StudentAddParam;
import upc.backend.controller.collect.Param.CollectAddParam;
import upc.backend.controller.datasets.Param.DatasetsAddParam;
import upc.backend.controller.datasets.Param.DatasetsUpdateParam;
import upc.backend.controller.file.param.FileAddParam;
import upc.backend.entity.*;
import upc.backend.service.*;
import upc.backend.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/api")
public class DatasetsManagement {
    @Autowired
    private StandardServletMultipartResolver standardServletMultipartResolver;
    @Resource
    private DatasetsService datasetsService;
    @Resource
    private CollectService collectService;
    @Resource
    private DatasetsteamJointService datasetsteamJointService;
    @Resource
    private TeamService teamService;
    @Resource
    private TeamfileService teamfileService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/dataset/list", method = RequestMethod.GET)
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
                return ResultGenerator.genSuccessResult(datasetsService.getDatasetsPage(pageUtil));
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }

    @RequestMapping(value = "/dataset/listmanage", method = RequestMethod.GET)
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
                return ResultGenerator.genSuccessResult(datasetsService.getDatasetsPageByUserIdOrderByTime(pageUtil));
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }

    @RequestMapping(value = "/dataset/listByTeam", method = RequestMethod.GET)
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
                return ResultGenerator.genSuccessResult(datasetsService.getDatasetsPageByUserIdOrderByTeamId(pageUtil));
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }


    @RequestMapping(value = "/dataset/collect", method = RequestMethod.DELETE)
    //@ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result deletecollect(@RequestBody BatchIdParam batchIdParam) {

        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        collectService.deleteBatchByDataId(batchIdParam.getIds());
        if (collectService.deleteBatch(batchIdParam.getIds())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    @RequestMapping(value = "/dataset", method = RequestMethod.DELETE)
    //@ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam) {

        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (datasetsService.deleteBatch(batchIdParam.getIds())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    @RequestMapping(value = "/dataset/{dataId}", method = RequestMethod.GET)
// "获取单条信息", "根据id查询"
    public Result info(HttpServletRequest httpServletRequest , @PathVariable("dataId") Integer dataId,
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
                    Datasets datasets = datasetsService.getDatasetsByDataId(dataId);
                    if (datasets == null) {
                        return ResultGenerator.genFailResult("未查询到数据");
                    }
                    // 使用 DATASETS_UPLOAD_DIC 构造文件的本地路径
                    String datasetUrl = datasets.getUrl();
                    if (datasetUrl.startsWith("/upload/datasets/")) {
                        datasetUrl = datasetUrl.substring("/upload/datasets/".length()); // 移除重复部分
                    }
                    String localFilePath = Constants.DATASETS_UPLOAD_DIC + datasetUrl;
                    File file = new File(localFilePath);
                    // 获取文件大小
                    long fileSize = file.exists() ? file.length() : 0; // 确保文件存在再获取大小
                    datasets.setFileSize(fileSize);

                    String userName = userService.getUserNameByUserId(datasets.getUserId());
                    datasets.setUserName(userName);

                    //获取CollectId
                    Map<String, Object> params = new HashMap<>(10);
                    params.put("userid", userToken.getUserId());
                    params.put("dataid", datasets.getDataId());
                    CollectQueryUtil collectUtil = new CollectQueryUtil(params);
                    Collect collect = collectService.getCollectByDataId(collectUtil);
                    if (collect != null) {
                        datasets.setCollectId(collect.getCollectId());
                    } else {
                        datasets.setCollectId(null); // 或者可以选择其他处理方式
                    }

                    if (datasets == null) {
                        return ResultGenerator.genFailResult("未查询到数据");
                    }
                    Map<String, Object> result = new HashMap<>();
                    result.put("datasets", datasets);
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

    @RequestMapping(value = "/dataset/teamname/{dataId}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result team(@PathVariable("dataId") Integer dataId) {
        Datasets datasets = datasetsService.getDatasetsByDataId(dataId);
        Team team = teamService.getTeamByTeamId(datasets.getTeamId());
        return ResultGenerator.genSuccessResult(team);
    }

    //修改
    @RequestMapping(value = "/dataset", method = RequestMethod.PUT)
// @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result update(@RequestBody DatasetsUpdateParam datasetsUpdateParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                Datasets datasets = new Datasets();
                BeanUtil.copyProperties(datasetsUpdateParam, datasets);
                datasets.setUploadTime(new Date());
                if(datasetsService.updateDatasetsInfo(datasets)){
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

    @RequestMapping(value = "/dataset/detail", method = RequestMethod.PUT)
// @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result updatedetail(@RequestBody DatasetsUpdateParam datasetsUpdateParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                Datasets datasets = new Datasets();
                BeanUtil.copyProperties(datasetsUpdateParam, datasets);
                if(datasetsService.updateDatasetsInfo(datasets)){
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

    @RequestMapping(value = "/dataset/collect", method = RequestMethod.POST)
    public Result save(@RequestBody CollectAddParam collectAddParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                if (collectAddParam.getDataId()==null ) {
                    return ResultGenerator.genFailResult("参数异常！");
                }
                else {
                    Collect collect = new Collect();
                    collect.setDataId(collectAddParam.getDataId());
                    collect.setUserId(userToken.getUserId());
                    collect.setCollectType("Datasets");
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
