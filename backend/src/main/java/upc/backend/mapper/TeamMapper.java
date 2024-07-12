package upc.backend.mapper;
import upc.backend.entity.Team;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface TeamMapper {
    int insert(Team team);

    int insertSelective(Team team);

    String getTeamNameByTeamId(Integer TeamId);

    List<Team> selectByTeamIds(List<Integer> TeamIds);

    Team selectByTeamId(Integer TeamId);

    int updateByPrimaryKeySelective(Team team);

    int updateByPrimaryKey(Team team);

    List<Team> findAllTeamList(PageQueryUtil pageUtil);
    int getNumOfTotalTeam(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids);

}
