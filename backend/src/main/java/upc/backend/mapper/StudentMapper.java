package upc.backend.mapper;
import org.apache.ibatis.annotations.Param;
import upc.backend.entity.Student;
import upc.backend.entity.User;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface StudentMapper {
    int insert(Student student);
    int userIdExists(Integer UserId);
    int insertSelective(Student student);
    int insertTeamFromStudent(Student team);

    List<Student> selectByName(String name);
    List<Student> selectByClass(String Class);
    Student selectByStunum(Integer id);

    int updateByPrimaryKeySelective(Student student);

    int updateByUserTeamKeySelective(Student student);

    List<Student> findAllStudentList(PageQueryUtil pageUtil);
    int getNumOfTotalStudentByTeacherTeam(PageQueryUtil pageUtil);
    List<Student> findTeamAllStudentList(PageQueryUtil pageUtil);
    int getNumOfTotalStudentBySelectTeam(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids,Integer[] teamId);
    int lockStudentBatch(@Param("ids") Integer[] ids, @Param("lockStatus") int lockStatus);
    List<String> getNameOptions(Integer UserId);
    Student selectByPrimaryKey(Integer UserId,Integer TeamId);
    int resetPassword(Integer UserId,String UserPassword);
}
