package upc.backend.controller.code;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import upc.backend.common.BatchIdParam;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.datasets.Param.DatasetsUpdateParam;
import upc.backend.controller.file.param.FileAddParam;
import upc.backend.entity.Code;
import upc.backend.entity.Team;
import upc.backend.entity.User;
import upc.backend.entity.UserToken;
import upc.backend.service.*;
import upc.backend.util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/api")
public class CodeManagement {
    @Autowired
    private StandardServletMultipartResolver standardServletMultipartResolver;
    @Resource
    private CodeService codeService;
    @Resource
    private TeamService teamService;
    @Resource
    private TeamfileService teamfileService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/code/list", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestParam(required = false) Integer CodeId,
                       @RequestParam(required = false) Integer UserId,
                       @RequestParam(required = false) Integer TeamId) {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 5) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map<String, Object> params = new HashMap<>(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        params.put("CodeId", CodeId);
        params.put("UserId", UserId);
        params.put("TeamId", TeamId);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(codeService.getCodePage(pageUtil));
    }


    @RequestMapping(value = "/code/{codeId}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result info(@PathVariable("codeId") Integer codeId) {
        Code code = codeService.getCodeByCodeId(codeId);
        if (code == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(code);
    }

    @RequestMapping(value = "/code/username/{codeId}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result user(@PathVariable("codeId") Integer codeId) {
        Code code = codeService.getCodeByCodeId(codeId);
        User user = userService.getUserByUserId(code.getUserId());
        return ResultGenerator.genSuccessResult(user);
    }

    @RequestMapping(value = "/code/teamname/{codeId}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result team(@PathVariable("codeId") Integer codeId) {
        Code code = codeService.getCodeByCodeId(codeId);
        Team team = teamService.getTeamByTeamId(code.getTeamId());
        return ResultGenerator.genSuccessResult(team);
    }
}
