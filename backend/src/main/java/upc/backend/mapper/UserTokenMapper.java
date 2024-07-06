package upc.backend.mapper;
import upc.backend.entity.UserToken;

public interface UserTokenMapper {
    int deleteByPrimaryKey(Integer userid);
    int insert(UserToken record);
    int insertSelective(UserToken record);
    UserToken selectByPrimaryKey(Integer userid);
    UserToken selectByToken(String token);
    int updateByPrimaryKeySelective(UserToken record);
    int updateByPrimaryKey(UserToken record);
}
