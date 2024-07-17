package upc.backend.controller.video;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.BatchIdParam;
import upc.backend.entity.UserToken;
import upc.backend.entity.Video;
import upc.backend.service.UserTokenService;
import upc.backend.service.videoService;
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
    private UserTokenService userTokenService;

//获取单个详情信息
    @RequestMapping(value = "api/videos/{videoId}", method = RequestMethod.GET)
    public Result info(@PathVariable("videoId") Integer VideoId) {
        Video video = videoService.getVideoById(VideoId);
        if (video == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(video);
    }
//    @GetMapping("api/videos/${VideoId}")
//    public ResponseEntity<Result> getVideoById(@PathVariable("VideoId") Integer VideoId) {
//        video video = videoService.getVideoById(VideoId);
//        if (video == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResultGenerator.genFailResult("未查询到数据"));
//        }
//        return ResponseEntity.ok(ResultGenerator.genSuccessResult(video));
//    }
//获取分页
    @RequestMapping(value = "/api/videos", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestHeader("token") String str_token)

    {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 8) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        UserToken userToken = userTokenService.selectByToken(str_token);
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        params.put("userId", userToken.getUserId());
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(videoService.getVideosPage(pageUtil));
    }


    @RequestMapping(value = "api/videos", method = RequestMethod.DELETE)
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

    @RequestMapping(value = "/videos", method = RequestMethod.GET)
    public Result search(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestHeader("token") String str_token,
                         @RequestParam(required = false) String searchQuery,
                         @RequestParam(defaultValue = "VideoName") String category)
    {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 8) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        UserToken userToken = userTokenService.selectByToken(str_token);
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        params.put("userid", userToken.getUserId());
        params.put("category", category);
        params.put("searchQuery",searchQuery);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(videoService.getVideoPage(pageUtil));
    }


}
