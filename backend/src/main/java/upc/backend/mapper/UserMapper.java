package upc.backend.mapper;
import org.apache.ibatis.annotations.Param;
import upc.backend.entity.User;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    User login(@Param("username") String username, @Param("password") String password);

    User selectByPrimaryKey(Integer userid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> findAllUserList(PageQueryUtil pageUtil);
    int getNumOfTotalUsers(PageQueryUtil pageUtil);
    int lockUserBatch(@Param("ids") Integer[] ids, @Param("lockStatus") int lockStatus);
}
