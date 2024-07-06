package upc.backend.controller.aa;

// 引入所需的包
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
import upc.backend.controller.file.param.FileAddParam;
import upc.backend.entity.Reference;
import upc.backend.entity.UploadFile;
import upc.backend.entity.User;
import upc.backend.entity.UserToken;
import upc.backend.service.FileUploadService;
import upc.backend.service.UserService;
import upc.backend.service.UserTokenService;
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
public class fileupload {
    @Autowired
    private StandardServletMultipartResolver standardServletMultipartResolver;

    // 注入文件上传服务
    @Resource
    private FileUploadService fileUploadService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private UserService userService;

    // 处理文件上传请求
    @RequestMapping(value = "/aa/files", method = RequestMethod.POST)
    public Result upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws URISyntaxException, FileNotFoundException {
        List<String> multipartData = new ArrayList<>(2);
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
            multipartData.add(Utils.getHost(new URI(httpServletRequest.getRequestURL() + "")).toString());
            multipartData.add("/upload/files/" + newFileName);
            resultSuccess.setData(multipartData);
            //resultSuccess.setData(Utils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/files/" + newFileName); // 设置返回的数据
            return resultSuccess; // 返回成功结果
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("文件上传失败"); // 返回失败结果
        }
    }

    // 处理图片上传请求
    @RequestMapping(value = "/aa/images", method = RequestMethod.POST)
    public Result uploadV2(HttpServletRequest httpServletRequest) throws URISyntaxException, FileNotFoundException {
        List<MultipartFile> multipartFiles = new ArrayList<>(8); // 创建一个存储多文件的列表，最大容量为8
        if (standardServletMultipartResolver.isMultipart(httpServletRequest)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) httpServletRequest;
            Iterator<String> iter = multiRequest.getFileNames(); // 获取上传的文件名
            int total = 0; // 计数器
            while (iter.hasNext()) {
                if (total > 5) {
                    return ResultGenerator.genFailResult("最多上传5张图片"); // 限制最多上传5张图片
                }
                total += 1;
                MultipartFile file = multiRequest.getFile(iter.next()); // 获取每一个文件
                multipartFiles.add(file); // 将文件添加到列表
            }
        }
        if (CollectionUtils.isEmpty(multipartFiles)) {
            return ResultGenerator.genFailResult("参数异常"); // 如果没有文件，返回失败结果
        }
        if (multipartFiles != null && multipartFiles.size() > 5) {
            return ResultGenerator.genFailResult("最多上传5张图片"); // 再次检查文件数量
        }
        List<String> fileNames = new ArrayList(multipartFiles.size()); // 创建一个列表存储文件名
        for (int i = 0; i < multipartFiles.size(); i++) {
            String fileName = multipartFiles.get(i).getOriginalFilename(); // 获取原始文件名
            String suffixName = fileName.substring(fileName.lastIndexOf(".")); // 获取文件后缀
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss"); // 日期格式化
            Random r = new Random(); // 随机数生成器
            StringBuilder tempName = new StringBuilder();
            tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName); // 生成唯一文件名
            String newFileName = tempName.toString(); // 转换为字符串
            File destFile = new File(Constants.IMGS_UPLOAD_DIC + newFileName); // 创建目标文件
            try {
                multipartFiles.get(i).transferTo(destFile); // 保存文件
                fileNames.add(Utils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/images/" + newFileName); // 添加文件路径到列表
            } catch (IOException e) {
                e.printStackTrace();
                return ResultGenerator.genFailResult("文件上传失败"); // 返回失败结果
            }
        }
        Result resultSuccess = ResultGenerator.genSuccessResult(); // 生成成功结果
        resultSuccess.setData(fileNames); // 设置返回的数据
        return resultSuccess; // 返回成功结果
    }

    // 保存文件信息
    @RequestMapping(value = "/aa/file_save", method = RequestMethod.POST)
    public Result save(@RequestBody FileAddParam fileAddParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token); // 根据token获取用户信息
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult()); // 未登录错误
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult()); // token过期错误
            } else {
                User user = userService.getUserDetailById(userToken.getUserid()); // 获取用户详情
                UploadFile upload_file = new UploadFile(); // 创建上传文件对象
                BeanUtil.copyProperties(fileAddParam, upload_file); // 复制属性
                upload_file.setUpload_user(user.getUsername()); // 设置上传用户
                upload_file.setUpload_time(new Date()); // 设置上传时间
                Boolean fileUpload_flag = fileUploadService.add_file(upload_file); // 添加文件信息
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
}
