package upc.backend.mapper;
import org.apache.ibatis.annotations.Param;
import upc.backend.entity.Team;
import upc.backend.entity.User;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    String getUserNameByUserId(Integer UserId);
    User login(@Param("UserName") String UsernName, @Param("UserPassword") String UserPassword);

    User selectByPrimaryKey(Integer UserId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> findAllUserList(PageQueryUtil pageUtil);
    int getNumOfTotalUsers(PageQueryUtil pageUtil);
    int lockUserBatch(@Param("ids") Integer[] ids, @Param("lockStatus") int lockStatus);

    int deleteBatch(Integer[] ids);


    User selectByUserId(Integer userId);
}
