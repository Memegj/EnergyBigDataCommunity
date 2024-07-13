package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Student;
import upc.backend.mapper.StudentMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;

@Service
public class StudentService {
    @Resource
    private StudentMapper studentMapper;


    // 获取学生信息
    public Student getStudentByStunum(Integer id){
        return studentMapper.selectByStunum(id);
    }

    //更新文献信息
    public Boolean updateStudentInfo(Student student){
        int radd = studentMapper.updateByPrimaryKeySelective(student);
        if (radd > 0){return true;}
        else {return false;}
    }

    public PageResult getStudentPage(PageQueryUtil pageUtil){
        List<Student> student = studentMapper.findAllStudentList(pageUtil);
        int total = studentMapper.getNumOfTotalStudent(pageUtil);
        PageResult pageResult = new PageResult(student, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public Boolean add_student(Student student){
        int radd = studentMapper.insertSelective(student);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return studentMapper.deleteBatch(ids) > 0;
    }
}
