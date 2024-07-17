package upc.backend.controller.video;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.BatchIdParam;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.Param.CollectAddParam;
import upc.backend.controller.datasets.Param.DatasetsUpdateParam;
import upc.backend.controller.video.param.VideoUpdateParam;
import upc.backend.entity.Collect;
import upc.backend.entity.Datasets;
import upc.backend.entity.UserToken;
import upc.backend.entity.Video;
import upc.backend.entity.Videocontent;
import upc.backend.service.CollectService;
import upc.backend.service.UserService;
import upc.backend.service.UserTokenService;
import upc.backend.service.VideoService;
import upc.backend.util.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@MapperScan("upc.backend.mapper")
@Controller
@CrossOrigin
@ResponseBody
@Slf4j
@RequestMapping("/api")
public class VideoManagement {
    @Resource
    private VideoService videoService;
    @Resource
    private CollectService collectService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/videodetail/{videoId}", method = RequestMethod.GET)
// "获取单条信息", "根据id查询"
    public Result info(HttpServletRequest httpServletRequest , @PathVariable("videoId") Integer videoId,
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
                    Video video = videoService.getVideoByVideoId(videoId);

                    if (video == null) {
                        return ResultGenerator.genFailResult("未查询到数据");
                    }

                    //获取CollectId
                    Map<String, Object> params = new HashMap<>(10);
                    params.put("userid", userToken.getUserId());
                    params.put("videoid", video.getVideoId());
                    CollectQueryUtil collectUtil = new CollectQueryUtil(params);
                    Collect collect = collectService.getCollectByVideoId(collectUtil);
                    List<Videocontent> videocontent = videoService.getVideoContentsByVideoId(videoId);
                    if (collect != null) {
                        video.setCollectId(collect.getCollectId());
                    } else {
                        video.setCollectId(null); // 或者可以选择其他处理方式
                    }

                    if (video == null) {
                        return ResultGenerator.genFailResult("未查询到数据");
                    }
                    Map<String, Object> result = new HashMap<>();
                    result.put("videocontent",videocontent);
                    result.put("video", video);
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

    //获取单个详情信息
/*    @RequestMapping(value = "api/videos/{videoId}", method = RequestMethod.GET)
    public Result info(@PathVariable("videoId") Integer VideoId) {
        Video video = videoService.getVideoById(VideoId);

        if (video == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(video);
    }*/

    //修改
    @RequestMapping(value = "/video", method = RequestMethod.PUT)
// @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result update(@RequestBody VideoUpdateParam videoUpdateParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                Video video = new Video();
                BeanUtil.copyProperties(videoUpdateParam, video);
                video.setUploadTime(new Date());
                if(videoService.updateVideoInfo(video)){
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
    @RequestMapping(value = "/video/collect", method = RequestMethod.DELETE)
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

    @RequestMapping(value = "/video/collect", method = RequestMethod.POST)
    public Result save(@RequestBody CollectAddParam collectAddParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                if (collectAddParam.getVideoId()==null ) {
                    return ResultGenerator.genFailResult("参数异常！");
                }
                else {
                    Collect collect = new Collect();
                    collect.setVideoId(collectAddParam.getVideoId());
                    collect.setUserId(userToken.getUserId());
                    collect.setCollectType("视频");
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
    @RequestMapping(value = "/video", method = RequestMethod.DELETE)
    //@ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam) {

        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (videoService.deleteBatch(batchIdParam.getIds())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }


    //获取分页
    @RequestMapping(value = "/video", method = RequestMethod.GET)
    public Result list(HttpServletRequest httpServletRequest , @RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestParam(required = false) String searchQuery,
                       @RequestParam(defaultValue = "VideoName") String category
    ) throws URISyntaxException {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 8) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        URI requestUrl = new URI(httpServletRequest.getRequestURL().toString());
        URI hostUrl = new URI(requestUrl.getScheme(), requestUrl.getUserInfo(), requestUrl.getHost(), requestUrl.getPort(), null, null, null);
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        params.put("category", category);
        params.put("searchQuery",searchQuery);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        PageResult pageresult = videoService.getVideosPage(pageUtil);
        Map<String, Object> result = new HashMap<>();
        result.put("pageresult", pageresult);
        result.put("hostUrl", hostUrl.toString()); // 将URI转换为字符串
        return ResultGenerator.genSuccessResult(result);
    }

    //获取分页
    @RequestMapping(value = "/video_manage", method = RequestMethod.GET)
    public Result list(HttpServletRequest httpServletRequest ,
                       @RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestHeader("token") String str_token,
                       @RequestParam(required = false) String searchQuery,
                       @RequestParam(defaultValue = "VideoName") String category) throws URISyntaxException {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 8) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        URI requestUrl = new URI(httpServletRequest.getRequestURL().toString());
        URI hostUrl = new URI(requestUrl.getScheme(), requestUrl.getUserInfo(), requestUrl.getHost(), requestUrl.getPort(), null, null, null);
        UserToken userToken = userTokenService.selectByToken(str_token);
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        params.put("userId", userToken.getUserId());
        params.put("category", category);
        params.put("searchQuery",searchQuery);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        PageResult pageresult = videoService.getVideosPage(pageUtil);
        Map<String, Object> result = new HashMap<>();
        result.put("pageresult", pageresult);
        result.put("hostUrl", hostUrl.toString()); // 将URI转换为字符串
        return ResultGenerator.genSuccessResult(result);
    }

    @RequestMapping(value = "/videoo", method = RequestMethod.DELETE)
    public Result delete(@RequestBody BatchIdParam batchIdParam, @RequestHeader("token") String str_token) {
        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (videoService.deleteBatchVideocontent(batchIdParam.getIds())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    @RequestMapping(value = "/video_search", method = RequestMethod.GET)
    public Result search(HttpServletRequest httpServletRequest ,
                         @RequestParam(required = false) Integer pageNumber,
                         @RequestParam(required = false) Integer pageSize,
                         @RequestHeader("token") String str_token,
                         @RequestParam(required = false) String searchQuery,
                         @RequestParam(defaultValue = "VideoName") String category) throws URISyntaxException {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 8) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        URI requestUrl = new URI(httpServletRequest.getRequestURL().toString());
        URI hostUrl = new URI(requestUrl.getScheme(), requestUrl.getUserInfo(), requestUrl.getHost(), requestUrl.getPort(), null, null, null);
        UserToken userToken = userTokenService.selectByToken(str_token);
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        params.put("userid", userToken.getUserId());
        params.put("category", category);
        params.put("searchQuery",searchQuery);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        PageResult pageresult =videoService.getVideoPage(pageUtil);
        Map<String, Object> result = new HashMap<>();
        result.put("pageresult", pageresult);
        result.put("hostUrl", hostUrl.toString()); // 将URI转换为字符串
        return ResultGenerator.genSuccessResult(result);
    }

}
