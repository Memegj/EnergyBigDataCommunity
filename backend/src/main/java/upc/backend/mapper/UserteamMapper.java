package upc.backend.mapper;
import upc.backend.entity.Userteam;
import upc.backend.util.PageQueryUtil;

import java.util.List;
import java.util.Map;

public interface UserteamMapper {
    int insert(Userteam userteam);

    int insertSelective(Userteam userteam);


    List<Userteam> selectByUserId(Integer UserId);
    List<Userteam> selectByPageUtil(PageQueryUtil pageUtil);

    int updateByPrimaryKeySelective(Userteam userteam);

    int updateByPrimaryKey(Userteam userteam);
    int insertUserTeam(Userteam userteam);
    void deleteByTeamIdAndUserId(Map<String, Object> params);
    List<Userteam> findAllUserteamList(PageQueryUtil pageUtil);
    int getNumOfTotalUserteam(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids);

}
