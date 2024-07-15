package upc.backend.controller.team;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import upc.backend.controller.team.param.TeamAddParam;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.common.BatchIdParam;
import upc.backend.controller.team.param.TeamUpdateParam;
import upc.backend.entity.*;
import upc.backend.service.TeamService;
import upc.backend.service.UserService;
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
public class TeamManagement {
    @Resource
    private TeamService teamService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private UserService userService;
    @RequestMapping(value = "/team/list", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize, @RequestHeader("token") String str_token) {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return ResultGenerator.genFailResult("参数异常！");
        }

        UserToken userToken = userTokenService.selectByToken(str_token);
        if (userToken == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
            return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
        }

        Integer UserId = userToken.getUserId(); // 获取 userId
        Map params = new HashMap(8);
        params.put("UserId", UserId);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(teamService.getTeamPage(pageUtil));
    }
    @RequestMapping(value = "/team", method = RequestMethod.DELETE)
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

        if (teamService.deleteBatch(longIds, userId)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    @RequestMapping(value = "/team/{teamId}", method = RequestMethod.GET)
// "获取单条信息", "根据excelid查询"
    public Result info(@PathVariable("teamId") Integer teamId) {
        Team team = teamService.getTeamByTeamId(teamId);
        if (team == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(team);
    }
    // 新增文献

    @RequestMapping(value = "/team", method = RequestMethod.POST)
    public Result save(@RequestBody TeamAddParam teamAddParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            }
            else {
                Integer userId = userToken.getUserId();
               // User user = userService.getUserDetailById(userToken.getUserId());
                Team team= new Team();

                BeanUtil.copyProperties(teamAddParam,team);

                team.setRegisterTime(new Date());

                Boolean result_state = teamService.addTeamAndUserTeam(team, userId);
                if (result_state) {
                    Result result = new ResultGenerator().genSuccessResult();
                    result.setData(true);
                    return result;
                }
                else return ResultGenerator.genFailResult(ServiceResultEnum.DB_ERROR.getResult());
            }
        }
        else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }
    @RequestMapping(value = "/team", method = RequestMethod.PUT)
// @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result update(@RequestBody TeamUpdateParam teamUpdateParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            }
            else {
             //   User user = userService.getUserDetailById(userToken.getUserId());
                Team team = new Team();
                BeanUtil.copyProperties(teamUpdateParam, team);
              //  team.setUpload_user(user.getUsername());
                team.setRegisterTime(new Date());
                if(teamService.updateReferenceInfo(team)){
                    Result result = new ResultGenerator().genSuccessResult();
                    result.setData(true);
                    return result;
                }
                else return ResultGenerator.genFailResult("失败");
            }
        }
        else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }
}