package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Student;
import upc.backend.entity.User;
import upc.backend.mapper.StudentMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;

@Service
public class StudentService {
    @Resource
    private StudentMapper studentMapper;


   // 获取学生信息
    public Student getStudentDetailById(Integer UserId,Integer TeamId){
        return studentMapper.selectByPrimaryKey(UserId,TeamId);
    }
    public Student getStudentByStunum(Integer id){
        return studentMapper.selectByStunum(id);
    }
    //更新文献信息
    public Boolean updateStudentInfo(Student student){
        int radd = studentMapper.updateByPrimaryKeySelective(student);
        if (radd > 0){return true;}
        else {return false;}
    }
    public Boolean updateStudentTeam(Student student){
        int radd = studentMapper.updateByUserTeamKeySelective(student);
        if (radd > 0){return true;}
        else {return false;}
    }

    public PageResult getStudentPage(PageQueryUtil pageUtil){
        List<Student> student = studentMapper.findAllStudentList(pageUtil);
        int total = studentMapper.getNumOfTotalStudentByTeacherTeam(pageUtil);
        PageResult pageResult = new PageResult(student, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public PageResult getStudentTeamPage(PageQueryUtil pageUtil){
        List<Student> student = studentMapper.findTeamAllStudentList(pageUtil);
        int total = studentMapper.getNumOfTotalStudentBySelectTeam(pageUtil);
        PageResult pageResult = new PageResult(student, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public boolean userIdExists(Integer UserId){
        int radd=studentMapper.userIdExists(UserId);
        // 如果存在，则不执行任何操作
        if (radd > 0){return true;}
        else {return false;}
            }
    public Boolean add_student(Student student){
        int radd = studentMapper.insertSelective(student);
        if (radd > 0){return true;}
        else {return false;}
    }
    public Boolean add_student_team(Student team){
        int radd= studentMapper.insertTeamFromStudent(team);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean deleteBatch(Integer[] ids,Integer[] teamId) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return studentMapper.deleteBatch(ids,teamId) > 0;
    }
    public Boolean lockStudents(Integer[] ids, int lockStatus){
        if (ids.length < 1) {
            return false;
        }
        return studentMapper.lockStudentBatch(ids, lockStatus) > 0;
    }
    public Boolean resetPassword(Integer UserId, String UserPassword) {


        return studentMapper.resetPassword(UserId,UserPassword) > 0;
    }
    public List<String> getNameOptions(Integer UserId){
        return studentMapper.getNameOptions(UserId);
    }
}
