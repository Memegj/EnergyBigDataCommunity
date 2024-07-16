package upc.backend.controller.video;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.BatchIdParam;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.Param.CollectAddParam;
import upc.backend.entity.Collect;
import upc.backend.entity.UserToken;
import upc.backend.entity.Video;
import upc.backend.service.CollectService;
import upc.backend.service.UserService;
import upc.backend.service.UserTokenService;
import upc.backend.service.VideoService;
import upc.backend.util.CollectQueryUtil;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.Result;
import upc.backend.util.ResultGenerator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

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
                    if (collect != null) {
                        video.setCollectId(collect.getCollectId());
                    } else {
                        video.setCollectId(null); // 或者可以选择其他处理方式
                    }

                    if (video == null) {
                        return ResultGenerator.genFailResult("未查询到数据");
                    }
                    Map<String, Object> result = new HashMap<>();
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
    @RequestMapping(value = "/videos/{videoId}", method = RequestMethod.GET)
    public Result getVideoInfo(@PathVariable("videoId") Integer videoId,
                               @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                Integer userId = userToken.getUserId(); // 获取 userId
                Video video = videoService.getVideoByVideoId(videoId);
                //获取CollectId
                Map<String, Object> params = new HashMap<>(10);
                params.put("userid", userToken.getUserId());
                params.put("videoid", videoId);
                CollectQueryUtil collectUtil = new CollectQueryUtil(params);
                Collect collect = collectService.getCollectByVideoId(collectUtil);
                if (collect != null) {
                    video.setCollectId(collect.getCollectId());
                } else {
                    video.setCollectId(null); // 或者可以选择其他处理方式
                }

                if (video == null) {
                    return ResultGenerator.genFailResult("未查询到数据");
                }

                return ResultGenerator.genSuccessResult(video);
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
    @RequestMapping(value = "/videos/collect", method = RequestMethod.DELETE)
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

    @RequestMapping(value = "/videos/collect", method = RequestMethod.POST)
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

    //获取分页
    @RequestMapping(value = "/videos", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestParam(required = false) String searchQuery,
                       @RequestParam(defaultValue = "VideoName") String category
    ) {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 8) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        params.put("category", category);
        params.put("searchQuery",searchQuery);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(videoService.getVideosPage(pageUtil));
    }

}
