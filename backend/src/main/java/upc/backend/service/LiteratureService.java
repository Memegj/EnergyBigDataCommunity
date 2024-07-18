package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Literature;
import upc.backend.entity.Userteam;
import upc.backend.mapper.LiteratureMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class LiteratureService {
    @Resource
    private LiteratureMapper literatureMapper;
    @Resource
    private TeamService teamService;
    @Resource
    private UserteamService userteamService;


    // 获取文献信息
    public Literature getLiteratureByLiterId(Integer LiterId){
        return literatureMapper.selectByLiterId(LiterId);
    }

    //更新文献信息
    public Boolean updateLiteratureInfo(Literature literature){
        int radd = literatureMapper.updateByPrimaryKeySelective(literature);
        if (radd > 0){return true;}
        else {return false;}
    }

    public List<Literature> getLiteraturedata() {
        List<Literature> LiteratureList = literatureMapper.getliteraturesdata();
        return LiteratureList;
    }
    public PageResult getLiteraturePageByUserId(PageQueryUtil pageUtil){
        List<Literature> literatures = literatureMapper.findAllLiteratureListByUserId(pageUtil);
        int total = literatureMapper.getNumOfTotalLiteratureByUserId(pageUtil);
        for (Literature literature : literatures) {
            Integer teamId = literature.getTeamId();
            if (teamId == null) {
                literature.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                literature.setTeamName(teamName);
            }
        }
        PageResult pageResult = new PageResult(literatures, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public List<Literature> getLiteratureByTeamId(PageQueryUtil pageUtil, Integer[] teamIdsArray){
        return literatureMapper.selectByTeamIdsByType(pageUtil,teamIdsArray);
    }

    public PageResult getLiteraturePage(PageQueryUtil pageUtil){
        List<Userteam> userteams = userteamService.getTeamByPageUtil(pageUtil);
        List<Integer> teamIds = new ArrayList<>();

        for (Userteam userteam : userteams) {
            Integer teamId = userteam.getTeamId();
            teamIds.add(teamId); // 将 teamId 存入 teamIds 列表
        }
        Integer[] teamIdsArray = teamIds.toArray(new Integer[0]);
        List<Literature> literatures = getLiteratureByTeamId(pageUtil,teamIdsArray);
        int total = literatureMapper.getNumOfUserLiteratureByType(pageUtil,teamIdsArray);
        for (Literature literature : literatures) {
            Integer teamId = literature.getTeamId();
            if (teamId == null) {
                literature.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                literature.setTeamName(teamName);
            }
        }
        PageResult pageResult = new PageResult(literatures, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public PageResult getLiteraturePageByUserIdOrderByTime(PageQueryUtil pageUtil){
        List<Literature> literatures = literatureMapper.findAllLiteratureListByUserIdOrderByTime(pageUtil);
        int total = literatureMapper.getNumOfTotalLiteratureByUserId(pageUtil);
        for (Literature literature : literatures) {
            Integer teamId = literature.getTeamId();
            if (teamId == null) {
                literature.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                literature.setTeamName(teamName);
            }
        }
        PageResult pageResult = new PageResult(literatures, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public PageResult getLiteraturePageByUserIdOrderByTeamId(PageQueryUtil pageUtil){
        List<Literature> literatures = literatureMapper.findAllLiteratureListByUserIdOrderByTeamId(pageUtil);
        int total = literatureMapper.getNumOfTotalLiteratureByUserId(pageUtil);
        for (Literature literature : literatures) {
            Integer teamId = literature.getTeamId();
            if (teamId == null) {
                literature.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                literature.setTeamName(teamName);
            }
        }
        PageResult pageResult = new PageResult(literatures, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public Boolean add_literature(Literature literature){
        int radd = literatureMapper.insertSelective(literature);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean add_file(Literature literature){
        return literatureMapper.insertSelective(literature) > 0;
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return literatureMapper.deleteBatch(ids) > 0;
    }
}
