package upc.backend.mapper;
import upc.backend.entity.Student;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface StudentMapper {
    int insert(Student student);

    int insertSelective(Student student);

    List<Student> selectByName(String name);
    List<Student> selectByClass(String Class);
    Student selectByStunum(Integer id);

    int updateByPrimaryKeySelective(Student student);

    int updateByPrimaryKey(Student student);

    List<Student> findAllStudentList(PageQueryUtil pageUtil);
    int getNumOfTotalStudent(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids);

}
