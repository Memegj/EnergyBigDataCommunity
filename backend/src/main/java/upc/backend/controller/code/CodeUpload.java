package upc.backend.controller.code;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.code.Param.CodeAddParam;
import upc.backend.controller.datasets.Param.DatasetsAddParam;
import upc.backend.entity.*;
import upc.backend.service.*;
import upc.backend.util.BeanUtil;
import upc.backend.util.Result;
import upc.backend.util.ResultGenerator;
import upc.backend.util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CodeUpload {
    @Autowired
    private StandardServletMultipartResolver standardServletMultipartResolver;

    @Resource
    private CodeService codeService;
    @Resource
    private UserteamService userteamService;
    @Resource
    private TeamService teamService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/code/files", method = RequestMethod.POST)
    public Result upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws URISyntaxException, FileNotFoundException {
        List<String> multipartData = new ArrayList<>(2);
        String fileName = file.getOriginalFilename(); // 获取上传文件的原始文件名
        String suffixName = fileName.substring(fileName.lastIndexOf(".")); // 提取文件的后缀名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss"); // 日期格式化，用于生成唯一文件名
        Random r = new Random(); // 随机数生成器
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName); // 生成唯一文件名
        String newFileName = tempName.toString(); // 转换为字符串
        File destFile = new File(Constants.CODE_UPLOAD_DIC + newFileName); // 创建目标文件
        try {
            file.transferTo(destFile); // 将上传的文件保存到目标文件
            Result resultSuccess = ResultGenerator.genSuccessResult(); // 生成成功结果
            multipartData.add(Utils.getHost(new URI(httpServletRequest.getRequestURL() + "")).toString());
            multipartData.add("/upload/code/" + newFileName);
            resultSuccess.setData(multipartData);
            //resultSuccess.setData(Utils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/files/" + newFileName); // 设置返回的数据
            return resultSuccess; // 返回成功结果
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("文件上传失败"); // 返回失败结果
        }
    }

    /**
     * 图片上传
     */
    @RequestMapping(value = "/code/images", method = RequestMethod.POST)
    //@ApiOperation(value = "多图上传", notes = "wangEditor图片上传")
    public Result uploadV2(HttpServletRequest httpServletRequest) throws URISyntaxException, FileNotFoundException {

        List<MultipartFile> multipartFiles = new ArrayList<>(8);
        if (standardServletMultipartResolver.isMultipart(httpServletRequest)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) httpServletRequest;
            Iterator<String> iter = multiRequest.getFileNames();
            int total = 0;
            while (iter.hasNext()) {
                if (total > 5) {
                    return ResultGenerator.genFailResult("最多上传5张图片");
                }
                total += 1;
                MultipartFile file = multiRequest.getFile(iter.next());
                multipartFiles.add(file);
            }
        }
        if (CollectionUtils.isEmpty(multipartFiles)) {
            return ResultGenerator.genFailResult("参数异常");
        }
        if (multipartFiles != null && multipartFiles.size() > 5) {
            return ResultGenerator.genFailResult("最多上传5张图片");
        }
        List<String> fileNames = new ArrayList(multipartFiles.size());
        for (int i = 0; i < multipartFiles.size(); i++) {
            String fileName = multipartFiles.get(i).getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //生成文件名称通用方法
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Random r = new Random();
            StringBuilder tempName = new StringBuilder();
            tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
            String newFileName = tempName.toString();
            //创建文件
            File destFile = new File(Constants.IMGS_UPLOAD_DIC + newFileName);
            try {
                multipartFiles.get(i).transferTo(destFile);
                fileNames.add(Utils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/images/" + newFileName);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultGenerator.genFailResult("文件上传失败");
            }
        }
        Result resultSuccess = ResultGenerator.genSuccessResult();
        resultSuccess.setData(fileNames);
        return resultSuccess;
    }
    @RequestMapping(value = "/code/file_save", method = RequestMethod.POST)
    //@ApiOperation(value = "新增商品信息", notes = "新增商品信息")
    public Result save(@RequestBody CodeAddParam codeAddParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            }
            else {
                User user = userService.getUserDetailById(userToken.getUserId());
                Code code = new Code();
                BeanUtil.copyProperties(codeAddParam, code);
                code.setUserId(user.getUserId());
                code.setUploadTime(new Date());
                Boolean codeUpload_flag = codeService.add_file(code);
                if (codeUpload_flag) {
                    Result result = new ResultGenerator().genSuccessResult();
                    result.setData(true);
                    return result;
                } else {
                    return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
                }
            }
        }
        else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }





    }

    @RequestMapping(value = "/code/teamlist", method = RequestMethod.GET)
    public Result list(@RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                User user = userService.getUserDetailById(userToken.getUserId());
                List<Userteam> userteams = userteamService.getTeamByUserId(user.getUserId());

                List<Integer> teamIds = new ArrayList<>();
                // 遍历Userteam对象列表，取出每个对象的TeamName放入teamNames列表
                for (Userteam userteam : userteams) {
                    teamIds.add(userteam.getTeamId());
                }
                List<Team> teams = new ArrayList<>();
                for (Integer teamId : teamIds) {
                    Team team = teamService.getTeamByTeamId(teamId);
                    if (team != null) {
                        teams.add(team);
                    }
                }

                    return ResultGenerator.genSuccessResult(teams);

            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }
}
