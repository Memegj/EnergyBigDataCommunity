package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Userteam;
import upc.backend.mapper.UserteamMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;

@Service
public class UserteamService {
    @Resource
    private UserteamMapper userteamMapper;

    // 获取文献信息
    public List<Userteam> getTeamByUserId(Integer UserId){

        return userteamMapper.selectByUserId(UserId);
    }
    public List<Userteam> getTeamByPageUtil(PageQueryUtil pageUtil){

        return userteamMapper.selectByPageUtil(pageUtil);
    }

    //更新文献信息
    public Boolean updateUserteamInfo(Userteam userteam){
        int radd = userteamMapper.updateByPrimaryKeySelective(userteam);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean add_userteam(Userteam userteam){
        int radd = userteamMapper.insertSelective(userteam);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return userteamMapper.deleteBatch(ids) > 0;
    }
}
