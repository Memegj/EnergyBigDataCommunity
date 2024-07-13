package upc.backend.controller.student;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.BatchIdParam;
import upc.backend.common.Constants;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.student.Param.StudentAddParam;
import upc.backend.controller.student.Param.StudentUpdateParam;
import upc.backend.controller.user.param.UserUpdateParam;
import upc.backend.entity.Student;
import upc.backend.entity.UserToken;
import upc.backend.service.UserService;
import upc.backend.service.StudentService;
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
public class StuManagement {
    private static final Logger logger = LoggerFactory.getLogger(StuManagement.class);
    @Resource
    private UserService userService;
    @Resource
    private StudentService studentService;
    @Resource
    private UserTokenService userTokenService;

    @RequestMapping(value = "/students/list", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestParam(required = false) Integer lockStatus,
                       @RequestHeader("token") String str_token) {
        //, @RequestHeader("token") String token
        // logger.info("this test tokon:{}", token);
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        UserToken userToken = userTokenService.selectByToken(str_token);
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        if (lockStatus != null) {
            params.put("studentStatus", lockStatus);
        }
        if (userToken.getUserId() != null) {
            params.put("userId", userToken.getUserId());
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(studentService.getStudentPage(pageUtil));
    }
    @RequestMapping(value = "/students/team", method = RequestMethod.GET)
    public Result team(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestParam(required = false) Integer lockStatus,
                       @RequestParam(required = false) Integer teamId,
                       @RequestHeader("token") String str_token) {
        //, @RequestHeader("token") String token
        // logger.info("this test tokon:{}", token);
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        UserToken userToken = userTokenService.selectByToken(str_token);
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        if(teamId!=null){
        params.put("TeamId",teamId);}
        if (lockStatus != null) {
            params.put("studentStatus", lockStatus);
        }
        if (userToken.getUserId() != null) {
            params.put("userId", userToken.getUserId());
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(studentService.getStudentTeamPage(pageUtil));
    }


    //学生禁用与解除禁用(0-未锁定 1-已锁定)
    @RequestMapping(value = "/students/{lockStatus}", method = RequestMethod.PUT)
    public Result lockStudent(@RequestBody BatchIdParam batchIdParam, @PathVariable int lockStatus) {
        //logger.info("user:{}", user.toString());
        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (lockStatus != 0 && lockStatus != 1) {
            return ResultGenerator.genFailResult("操作非法！");
        }
        if (studentService.lockStudents(batchIdParam.getIds(), lockStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("禁用失败");
        }
    }

    @RequestMapping(value = "/students/stu_add", method = RequestMethod.POST)
    public Result student_add(@RequestBody StudentAddParam studentAddParam) {
        logger.info("UserName:{}", studentAddParam.getUserName());
        if (studentAddParam.getUserName() == null || studentAddParam.getNickName() == null || studentAddParam.getUserPassword() == null) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if(studentService.userIdExists(studentAddParam.getUserId())){
            Student team=new Student();
            team.setTeamId(studentAddParam.getTeamId());
            team.setUserId(studentAddParam.getUserId());
            if (studentService.add_student_team(team)) {
                Result result = new ResultGenerator().genSuccessResult();
                result.setData(true);
                return result;
            } else return ResultGenerator.genFailResult("学生新增失败");
        }

            else {
            Student student = new Student();
            Student team=new Student();
            student.setUserId(studentAddParam.getUserId());
            student.setUserName(studentAddParam.getUserName());
            student.setNickName(studentAddParam.getNickName());
            student.setUserEmail(studentAddParam.getUserEmail());
            student.setUserCollege(studentAddParam.getUserCollege());
            student.setUserPassword(studentAddParam.getUserPassword());
            student.setTeamId(studentAddParam.getTeamId());
            student.setTeamName(studentAddParam.getTeamName());
            student.setUserLocked((byte) 0);
            student.setRegisterTime(new Date());
            student.setUser_role("student");
            team.setTeamId(studentAddParam.getTeamId());
            team.setUserId(studentAddParam.getUserId());
            if (studentService.add_student(student) && studentService.add_student_team(team)) {
                Result result = new ResultGenerator().genSuccessResult();
                result.setData(true);
                return result;
            } else return ResultGenerator.genFailResult("学生新增失败");
        }
    }
    @RequestMapping(value = "/students/get_name_options", method = RequestMethod.GET)
    public Result list(@RequestHeader("token") String str_token) {
        UserToken userToken = userTokenService.selectByToken(str_token);

            return ResultGenerator.genSuccessResult(studentService.getNameOptions(userToken.getUserId()));

    }

    @RequestMapping(value = "/students", method = RequestMethod.DELETE)
    //@ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam) {

        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (studentService.deleteBatch(batchIdParam.getIds(),batchIdParam.getTeamId())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    @RequestMapping(value = "/students/{userId}", method = RequestMethod.GET)
    // "获取单条信息", "根据id查询"
    public Result info(@PathVariable("userId") Integer UserId,
                       @RequestParam(value = "teamId", required = false) Integer TeamId){
       Student student = studentService.getStudentDetailById(UserId,TeamId);

        if (student == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(student);
    }
    //修改
    @RequestMapping(value = "/students/update", method = RequestMethod.PUT)
    // @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result update(@RequestBody StudentUpdateParam studentUpdateParam, @RequestHeader("token") String str_token) {
        if (null != str_token && !"".equals(str_token) && str_token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenService.selectByToken(str_token);
            if (userToken == null) {
                return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            } else if (userToken.getExpire_time().getTime() <= System.currentTimeMillis()) {
                return ResultGenerator.genFailResult(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            } else {
                Student student = new Student();
                BeanUtil.copyProperties(studentUpdateParam, student);
                studentService.updateStudentInfo(student);
                studentService.updateStudentTeam(student);

                Result result = new ResultGenerator().genSuccessResult();
                result.setData(true);
                return result;

            }
        } else {
            return ResultGenerator.genFailResult(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
    }
    @RequestMapping(value = "/students/password", method= RequestMethod.PUT)
    // @ApiOperation(value = "重置用户密码", notes = "将指定用户的密码重置为'123456'")
    public Result resetPassword(@RequestBody StudentUpdateParam studentUpdateParam) {
        if (studentUpdateParam == null ) {
            return ResultGenerator.genFailResult("参数异常！");
        }

        if(studentService.resetPassword(studentUpdateParam.getUserId(), studentUpdateParam.getUserPassword())){
            return ResultGenerator.genSuccessResult("密码重置成功！");
        } else {
            return ResultGenerator.genFailResult("密码重置失败，请稍后再试。");
        }
    }
}
