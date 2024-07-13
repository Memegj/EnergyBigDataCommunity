package upc.backend.controller.datasets;

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
public class DatasetsUpload {
    @Autowired
    private StandardServletMultipartResolver standardServletMultipartResolver;

    @Resource
    private FileUploadService fileUploadService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/datasets/files", method = RequestMethod.POST)
    public Result uploadFile(HttpServletRequest httpServletRequest) throws URISyntaxException, FileNotFoundException {

        List<MultipartFile> multipartFiles = new ArrayList<>();
        if (standardServletMultipartResolver.isMultipart(httpServletRequest)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) httpServletRequest;
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next());
                multipartFiles.add(file);
            }
        }

        if (CollectionUtils.isEmpty(multipartFiles)) {
            return ResultGenerator.genFailResult("参数异常");
        }

        List<String> fileNames = new ArrayList<>(multipartFiles.size());
        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = multipartFile.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Random r = new Random();
            StringBuilder tempName = new StringBuilder();
            tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
            String newFileName = tempName.toString();
            File destFile = new File(Constants.DATASETS_UPLOAD_DIC + newFileName);
            try {
                multipartFile.transferTo(destFile);
                fileNames.add(Utils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/files/" + newFileName);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultGenerator.genFailResult("文件上传失败");
            }
        }

        Result resultSuccess = ResultGenerator.genSuccessResult();
        resultSuccess.setData(fileNames);
        return resultSuccess;
    }

    /**
     * 图片上传
     */
    @RequestMapping(value = "/datasets/images", method = RequestMethod.POST)
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
    @RequestMapping(value = "/datasets/file_save", method = RequestMethod.POST)
    //@ApiOperation(value = "新增商品信息", notes = "新增商品信息")
    public Result save(@RequestBody FileAddParam fileAddParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            }
            else {
                User user = userService.getUserDetailById(userToken.getUserId());
                UploadFile upload_file = new UploadFile();
                BeanUtil.copyProperties(fileAddParam, upload_file);
                upload_file.setUpload_user(user.getUserName());
                upload_file.setUpload_time(new Date());
                Boolean fileUpload_flag = fileUploadService.add_file(upload_file);
                if (fileUpload_flag) {
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

}
