package upc.backend.controller.videocontent;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.common.BatchIdParam;

import upc.backend.entity.*;
import upc.backend.service.VideoContentService;
import upc.backend.service.videoService;
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
public class VideoContent {
    @Resource
    private VideoContentService videoContentService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private videoService videoService;

    @RequestMapping(value = "/video/list", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestParam(required = false) Integer VideoId,
                       @RequestHeader("token") String str_token) {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return ResultGenerator.genFailResult("参数异常！");
        }

        UserToken userToken = userTokenService.selectByToken(str_token);
        if (userToken == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
            return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
        }
        Video videoId = videoService.getVideoById(VideoId);

        Map params = new HashMap(8);
        params.put("videoId", videoId);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(videoContentService.getVideoContentPage(pageUtil));
    }
}