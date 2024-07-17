package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Student;
import upc.backend.entity.Team;
import upc.backend.entity.Userteam;
import upc.backend.mapper.TeamMapper;
import upc.backend.mapper.UserteamMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeamService {
    @Resource
    private TeamMapper teamMapper;

    @Resource
    private UserteamMapper userteamMapper;

    // 获取文献信息
    public Team getTeamByTeamId(Integer TeamId){
        return teamMapper.selectByTeamId(TeamId);
    }

    //更新文献信息
    public Boolean updateTeamInfo(Team team){
        int radd = teamMapper.updateByPrimaryKeySelective(team);
        if (radd > 0){return true;}
        else {return false;}
    }
    public Boolean updateReferenceInfo(Team team){
        int radd = teamMapper.updateByPrimaryKeySelective(team);
        if (radd > 0){return true;}
        else {return false;}
    }

    public PageResult getTeamPage(PageQueryUtil pageUtil){
        List<Team> team = teamMapper.findAllTeamList(pageUtil);
        int total = teamMapper.getNumOfTotalTeam(pageUtil);
        PageResult pageResult = new PageResult(team, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public Boolean add_team(Team team){
        int radd = teamMapper.insertSelective(team);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean insertUserTeamById(Userteam userteam){
        int radd = userteamMapper.insertUserTeam(userteam);
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
            Userteam userteam = new Userteam();
            userteam.setUserId(UserId);
            userteam.setTeamId(TeamId);
            userteamMapper.insert(userteam);
            return true;
        }
        return false;
    }
    //    public PageResult getReferencesPage(PageQueryUtil pageUtil){
//        List<Team> team = teamMapper.findAllReferenceList(pageUtil);
//        int total = teamMapper.getNumOfTotalReferences(pageUtil);
//       // PageResult pageResult = new PageResult(excel, total, pageUtil.getLimit(), pageUtil.getPage());
//        return new pageResult(team, total, pageUtil.getLimit(), pageUtil.getPage());
//    }
    public PageResult getReferencesPage(PageQueryUtil pageUtil) {
        List<Team> team = teamMapper.findAllReferenceList(pageUtil);
        int total = teamMapper.getNumOfTotalReferences(pageUtil);
        return new PageResult(team, total, pageUtil.getLimit(), pageUtil.getPage());
    }
    public String getTeamNameByTeamId(Integer TeamId){
        return teamMapper.getTeamNameByTeamId(TeamId);
    }
    public List<Team> getTeamByTeamIds(List<Integer> teamIds) {
        return teamMapper.selectByTeamIds(teamIds);
    }

        public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return teamMapper.deleteBatch(ids) > 0;
    }
    public boolean deleteBatch(Long[] teamIds, Integer userId) {
        for (Long teamId : teamIds) {
            // 删除 user_team 表中与指定 teamId 和 userId 相关的记录
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("teamId", teamId);
            userteamMapper.deleteByTeamIdAndUserId(params);

            // 删除 team 表中的记录
            teamMapper.deleteTeamById(teamId);
        }
        return true;
    }
}
