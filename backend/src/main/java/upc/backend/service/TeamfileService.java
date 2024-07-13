package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Teamfile;
import upc.backend.mapper.TeamfileMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;

@Service
public class TeamfileService {
    @Resource
    private TeamfileMapper teamfileMapper;


    // 获取文献信息
    public List<Teamfile> getTeamByDataId(Integer DataId){

        return teamfileMapper.selectByDataId(DataId);
    }

    //更新文献信息
    public Boolean updateTeamfileInfo(Teamfile teamfile){
        int radd = teamfileMapper.updateByPrimaryKeySelective(teamfile);
        if (radd > 0){return true;}
        else {return false;}
    }

    public PageResult getStudentPage(PageQueryUtil pageUtil){
        List<Teamfile> teamfile = teamfileMapper.findAllTeamfileList(pageUtil);
        int total = teamfileMapper.getNumOfTotalTeamfile(pageUtil);
        PageResult pageResult = new PageResult(teamfile, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public Boolean add_teamfile(Teamfile teamfile){
        int radd = teamfileMapper.insertSelective(teamfile);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return teamfileMapper.deleteBatch(ids) > 0;
    }
}
