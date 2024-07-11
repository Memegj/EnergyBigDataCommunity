package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Team;
import upc.backend.entity.UserTeam;
import upc.backend.mapper.TeamMapper;
import upc.backend.mapper.UserTeamMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;

@Service
public class TeamService {
    @Resource
    private TeamMapper teamMapper;

    @Resource
    private UserTeamMapper userTeamMapper;

    // 获取文献信息
    public Team getReferenceById(Integer teamId){
        return teamMapper.selectByID(teamId);
    }

    public Boolean updateReferenceInfo(Team team){
        int radd = teamMapper.updateByPrimaryKeySelective(team);
        if (radd > 0){return true;}
        else {return false;}
    }
    public Boolean addTeamAndUserTeam(Team team, Integer UserId) {
        // 插入团队信息
        int result = teamMapper.insertSelective(team);
        if (result > 0) {
            // 获取插入后的团队ID
            Integer TeamId = team.getTeamId();
            // 插入 user_team 记录
            UserTeam userTeam = new UserTeam();
            userTeam.setUserId(UserId);
            userTeam.setTeamId(TeamId);
            userTeamMapper.insert(userTeam);
            return true;
        }
        return false;
    }
    public PageResult getReferencesPage(PageQueryUtil pageUtil){
        List<Team> excel = teamMapper.findAllReferenceList(pageUtil);
        int total = teamMapper.getNumOfTotalReferences(pageUtil);
        PageResult pageResult = new PageResult(excel, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public Boolean add_reference(Team team){
        int radd = teamMapper.insertSelective(team);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean insertUserTeamById(UserTeam userteam){
        int radd = teamMapper.insertUserTeam(userteam);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return teamMapper.deleteBatch(ids) > 0;
    }
}
