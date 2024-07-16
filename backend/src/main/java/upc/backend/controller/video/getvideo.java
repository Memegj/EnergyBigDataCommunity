package upc.backend.controller.video;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.ServiceResultEnum;
import upc.backend.entity.Collect;
import upc.backend.entity.UserToken;
import upc.backend.entity.Video;
import upc.backend.service.CollectService;
import upc.backend.service.UserTokenService;
import upc.backend.service.videoService;
import upc.backend.util.CollectQueryUtil;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.Result;
import upc.backend.util.ResultGenerator;

import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
@ResponseBody
@Slf4j
@RequestMapping("/api")
public class getvideo {
    @Resource
    private videoService videoService;
    @Resource
    private CollectService collectService;
    @Resource
    private UserTokenService userTokenService;
    @RequestMapping(value = "api/videos/{videoId}", method = RequestMethod.GET)
    public Result getVideoInfo(@PathVariable("videoId") Integer videoId,

                               @RequestHeader("token") String str_token) {


        UserToken userToken = userTokenService.selectByToken(str_token);
        if (userToken == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
            return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
        }

        Integer userId = userToken.getUserId(); // 获取 userId
        Video video = videoService.getVideoById(videoId);
        Map<String, Object> params = new HashMap<>(10);
        params.put("userid", userToken.getUserId());
        params.put("videoid", video.getVideoId());
        CollectQueryUtil collectUtil = new CollectQueryUtil(params);
        Collect collect = collectService.getCollectByUserIdAndVideoId(userId, videoId);
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

    //获取单个详情信息
/*    @RequestMapping(value = "api/videos/{videoId}", method = RequestMethod.GET)
    public Result info(@PathVariable("videoId") Integer VideoId) {
        Video video = videoService.getVideoById(VideoId);

        if (video == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(video);
    }*/
    @RequestMapping(value = "/collect/add", method = RequestMethod.POST)
    public Result addFavorite(@RequestParam("videoId") Integer videoId,
                              @RequestParam("userId") Integer userId) {
        Collect collect = new Collect();
        collect.setVideoId(videoId);
        collect.setUserId(userId);
        collectService.addCollect(collect);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/collect/remove", method = RequestMethod.POST)
    public Result removeFavorite(@RequestParam("videoId") Integer videoId,
                                 @RequestParam("userId") Integer userId) {
        Collect collect = collectService.getCollectByUserIdAndVideoId(userId, videoId);
        if (collect != null) {
            collectService.deleteById(collect.getCollectId());
        }
        return ResultGenerator.genSuccessResult();
    }

    //获取分页
    @RequestMapping(value = "/api/videos", method = RequestMethod.GET)
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
