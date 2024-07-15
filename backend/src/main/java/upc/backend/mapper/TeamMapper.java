package upc.backend.mapper;
import org.apache.ibatis.annotations.Delete;
import upc.backend.entity.Team;
import upc.backend.entity.UserTeam;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface TeamMapper {
    int insert(Team team);

    int insertSelective(Team team);
    int insertUserTeam(UserTeam userteam);

    List<Team> selectByTitle(String teamname);

    Team  selectByID(Integer teamId);

    String getTeamNameByTeamId(Integer TeamId);

    List<Team> selectByTeamIds(List<Integer> TeamIds);

    Team selectByTeamId(Integer TeamId);

    int updateByPrimaryKeySelective(Team team);

    int updateByPrimaryKey(Team team);
    List<Team> findAllReferenceList(PageQueryUtil pageUtil);
    int getNumOfTotalReferences(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids);

    List<Team> findAllTeamList(PageQueryUtil pageUtil);
    int getNumOfTotalTeam(PageQueryUtil pageUtil);


 //   @Delete("delete from team where TeamId = #{TeamId}")
    void deleteTeamById(Long teamId);

}
