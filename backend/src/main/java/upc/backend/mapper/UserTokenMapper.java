package upc.backend.mapper;
import upc.backend.entity.UserToken;

public interface UserTokenMapper {
    int deleteByPrimaryKey(Integer UserId);
    int insert(UserToken record);
    int insertSelective(UserToken record);
    UserToken selectByPrimaryKey(Integer UserId);
    UserToken selectByToken(String token);
    int updateByPrimaryKeySelective(UserToken record);
    int updateByPrimaryKey(UserToken record);
}
