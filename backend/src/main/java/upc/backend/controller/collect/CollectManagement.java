package upc.backend.controller.collect;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.BatchIdParam;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.team.param.TeamAddParam;
import upc.backend.controller.team.param.TeamUpdateParam;
import upc.backend.entity.Collect;
import upc.backend.entity.Team;
import upc.backend.entity.UserToken;
import upc.backend.service.CollectService;
import upc.backend.service.TeamService;
import upc.backend.service.UserService;
import upc.backend.service.UserTokenService;
import upc.backend.util.BeanUtil;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.Result;
import upc.backend.util.ResultGenerator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/api")
public class CollectManagement {
    @Resource
    private CollectService collectService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private UserService userService;
    @RequestMapping(value = "/collect/list", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestParam(required = false) String selectedCategory,

                       @RequestHeader("token") String str_token) {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 5) {
            return ResultGenerator.genFailResult("参数异常！");
        }

        UserToken userToken = userTokenService.selectByToken(str_token);
        if (userToken == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
            return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
        }

        Integer userId = userToken.getUserId(); // 获取 userId
        Map params = new HashMap(8);
        params.put("userId", userId);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        params.put("selectedCategory",selectedCategory);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        //return ResultGenerator.genSuccessResult(teamService.getTeamPage(pageUtil));
        return ResultGenerator.genSuccessResult(collectService.getCollectionByUserId(pageUtil));
    }

    @RequestMapping(value = "/collect/get_chartdata", method = RequestMethod.GET)
    public Result get_data2() {
        List<String> collectslist = collectService.getcollectTypes();
        List<Integer> countlist = collectService.gettypesCounts();

        HashMap<String, List> map = new HashMap<>();
        map.put("typeData", collectslist);
        map.put("collectionsamount", countlist);
        return ResultGenerator.genSuccessResult(map);
    }

    @RequestMapping(value = "/collect", method = RequestMethod.DELETE)
    public Result delete(@RequestBody BatchIdParam batchIdParam, @RequestHeader("token") String str_token) {
        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }

        UserToken userToken = userTokenService.selectByToken(str_token);
        if (userToken == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
            return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
        }

        Integer userId = userToken.getUserId(); // 获取 userId

        // 将 Integer[] 转换为 Long[]
        Integer[] intIds = batchIdParam.getIds();
        Long[] longIds = new Long[intIds.length];
        for (int i = 0; i < intIds.length; i++) {
            longIds[i] = intIds[i].longValue();
        }

        if (collectService.deleteBatch(longIds, userId)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

   }