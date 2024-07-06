package upc.backend.controller.aa;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.common.BatchIdParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upc.backend.controller.aa.StudentAddParam;
import upc.backend.controller.aa.StudentUpdateParam;
import upc.backend.controller.references.param.ReferenceAddParam;
import upc.backend.controller.user.UserManagement;
import upc.backend.entity.*;
import upc.backend.service.StudentService;
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
public class stu {
    private static final Logger logger = LoggerFactory.getLogger(stu.class);
    @Resource
    private StudentService studentService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/aa/stu", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize) {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(studentService.getStudentPage(pageUtil));
    }

    @RequestMapping(value = "/stu", method = RequestMethod.DELETE)
    //@ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam) {

        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (studentService.deleteBatch(batchIdParam.getIds())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    @RequestMapping(value = "/stu/{id}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result info(@PathVariable("id") Integer id) {
        Student student = studentService.getStudentByStunum(id);
        if (student == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(student);
    }

    // 新增文献
    @RequestMapping(value = "/stu", method = RequestMethod.POST)
    public Result save(@RequestBody StudentAddParam studentAddParam, @RequestHeader("token") String str_token) {
        logger.info("stuname:{}", studentAddParam.getStuname());
        if (studentAddParam.getStuname()==null || studentAddParam.getStuclass()==null ) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        else {
            Student student = new Student();
            student.setStuname(studentAddParam.getStuname());
            student.setStuclass(studentAddParam.getStuclass());
            if (studentService.add_student(student)) {
                Result result = new ResultGenerator().genSuccessResult();
                result.setData(true);
                return result;
            }
            else return ResultGenerator.genFailResult("用户新增失败");
        }
    }

    //修改
    @RequestMapping(value = "/stu", method = RequestMethod.PUT)
    // @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result update(@RequestBody StudentUpdateParam studentUpdateParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            }
            else {
                Student student = new Student();
                BeanUtil.copyProperties(studentUpdateParam, student);
                if(studentService.updateStudentInfo(student)){
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
