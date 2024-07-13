package upc.backend.controller.datasets;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import upc.backend.common.BatchIdParam;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.datasets.Param.DatasetsAddParam;
import upc.backend.controller.datasets.Param.DatasetsUpdateParam;
import upc.backend.controller.file.param.FileAddParam;
import upc.backend.entity.*;
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
public class DatasetsManagement {
    @Autowired
    private StandardServletMultipartResolver standardServletMultipartResolver;
    @Resource
    private DatasetsService datasetsService;
    @Resource
    private TeamService teamService;
    @Resource
    private TeamfileService teamfileService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/dataset/list", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize) {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 5) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(datasetsService.getDatasetsPage(pageUtil));
    }

    @RequestMapping(value = "/dataset", method = RequestMethod.DELETE)
    //@ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam) {

        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (datasetsService.deleteBatch(batchIdParam.getIds())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    @RequestMapping(value = "/dataset/{dataId}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result info(@PathVariable("dataId") Integer dataId) {
        Datasets datasets = datasetsService.getDatasetsByDataId(dataId);
        if (datasets == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(datasets);
    }

    @RequestMapping(value = "/dataset/teamname/{dataId}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result team(@PathVariable("dataId") Integer dataId) {
        Datasets datasets = datasetsService.getDatasetsByDataId(dataId);
        Team team = teamService.getTeamByTeamId(datasets.getTeamId());
        return ResultGenerator.genSuccessResult(team);
    }

    // 新增文献
    @RequestMapping(value = "/dataset/files", method = RequestMethod.POST)
    public Result upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws URISyntaxException, FileNotFoundException {
        String fileName = file.getOriginalFilename(); // 获取上传文件的原始文件名
        String suffixName = fileName.substring(fileName.lastIndexOf(".")); // 提取文件的后缀名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss"); // 日期格式化，用于生成唯一文件名
        Random r = new Random(); // 随机数生成器
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName); // 生成唯一文件名
        String newFileName = tempName.toString(); // 转换为字符串
        File destFile = new File(Constants.FILES_UPLOAD_DIC + newFileName); // 创建目标文件
        try {
            file.transferTo(destFile); // 将上传的文件保存到目标文件
            Result resultSuccess = ResultGenerator.genSuccessResult(); // 生成成功结果
            //multipartData.add(Utils.getHost(new URI(httpServletRequest.getRequestURL() + "")).toString());
            //multipartData.add("/upload/files/" + newFileName);
            //resultSuccess.setData(multipartData);
            resultSuccess.setData("/upload/files/" + newFileName); // 设置返回的数据
            return resultSuccess; // 返回成功结果
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("文件上传失败"); // 返回失败结果
        }
    }

    // 处理图片上传请求
    @RequestMapping(value = "/dataset/images", method = RequestMethod.POST)
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

    // 保存文件信息
    @RequestMapping(value = "/dataset/save", method = RequestMethod.POST)
    public Result save(@RequestBody FileAddParam fileAddParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token); // 根据token获取用户信息
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult()); // 未登录错误
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult()); // token过期错误
            } else {
                User user = userService.getUserDetailById(userToken.getUserId()); // 获取用户详情
                Datasets datasets = new Datasets(); // 创建上传文件对象
                BeanUtil.copyProperties(fileAddParam, datasets); // 复制属性
                datasets.setUserId(user.getUserId()); // 设置上传用户
                datasets.setUploadTime(new Date()); // 设置上传时间
                Boolean fileUpload_flag = datasetsService.add_file(datasets); // 添加文件信息
                if (fileUpload_flag) {
                    Result result = new ResultGenerator().genSuccessResult(); // 生成成功结果
                    result.setData(true);
                    return result; // 返回成功结果
                } else {
                    return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult()); // 操作错误
                }
            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult()); // 未登录错误
        }
    }

    //修改
    @RequestMapping(value = "/dataset", method = RequestMethod.PUT)
    // @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result update(DatasetsUpdateParam datasetsUpdateParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            }
            else {
                Datasets datasets = new Datasets();
                BeanUtil.copyProperties(datasetsUpdateParam, datasets);
                if(datasetsService.updateDatasetsInfo(datasets)){
                    Result result = new ResultGenerator().genSuccessResult();
                    result.setData(true);
                    return result;
                }
                else return ResultGenerator.genFailResult("学生信息更新失败");
            }
        }
        else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }

}
