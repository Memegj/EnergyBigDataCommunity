package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Student;
import upc.backend.entity.Team;
import upc.backend.entity.Teamfile;
import upc.backend.mapper.TeamMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;

@Service
public class TeamService {
    @Resource
    private TeamMapper teamMapper;


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
}
