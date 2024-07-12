package upc.backend.mapper;


import org.apache.ibatis.annotations.Delete;
import upc.backend.entity.Team;
import upc.backend.entity.UserTeam;
import upc.backend.util.PageQueryUtil;

import java.util.List;
import java.util.Map;

public interface UserTeamMapper {
    int insert(UserTeam record);
   // int deleteByTeamIds(Integer[] teamIds);
    int deleteByTeamIds(Integer[] ids);
  //  List<Team> deleteByTeamIds(PageQueryUtil pageUtil);
 // @Delete("delete from user_team where userId = #{UserId} and teamId = #{TeamId}")
  void deleteByTeamIdAndUserId(Map<String, Object> params);
}
