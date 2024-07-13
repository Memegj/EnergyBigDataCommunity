package upc.backend.controller.video;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.ServiceResultEnum;
import upc.backend.entity.Reference;
import upc.backend.entity.User;
import upc.backend.entity.video;
import upc.backend.service.ReferenceService;
import upc.backend.service.UserA_Service;
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

//获取单个详情信息
    @RequestMapping(value = "api/videos/{videoId}", method = RequestMethod.GET)
    public Result info(@PathVariable("videoId") Integer VideoId) {
        video video = videoService.getVideoById(VideoId);
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
                       @RequestParam(required = false) Integer pageSize
    ) {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 8) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(videoService.getVideosPage(pageUtil));
    }
    //搜索函数
//    @RequestMapping(value = "/api/video/getVideo", method = RequestMethod.GET)
//    public

//    public Result getVideo() {
//        //log.info("user token:{}", passwordParam.getToken());
//        User userA = userA_Service.selectByID(3);
//        if (userA != null) {
//            Result result= ResultGenerator.genSuccessResult();
//            result.setData(userA.getUserName());
//            return result;
//        } else {
//            return ResultGenerator.genFailResult(ServiceResultEnum.DB_ERROR.getResult());
//        }
//    }


}
