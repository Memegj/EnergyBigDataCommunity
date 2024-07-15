package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.user.param.UserUpdateParam;
import upc.backend.entity.Code;
import upc.backend.entity.Datasets;
import upc.backend.entity.UserToken;
import upc.backend.entity.Userteam;
import upc.backend.mapper.CodeMapper;
import upc.backend.mapper.UserTokenMapper;
import upc.backend.util.NumberUtil;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;
import upc.backend.util.SystemUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CodeService {
    private final CodeMapper codeMapper;
    @Resource
    private UserteamService userteamService;
    @Resource
    private TeamService teamService;

    public CodeService(CodeMapper codeMapper) {
        this.codeMapper = codeMapper;
    }

    public PageResult getCodePageByUserIdOrderByTime(PageQueryUtil pageUtil){
        List<Code> codes = codeMapper.findAllCodeListByUserIdOrderByTime(pageUtil);
        int total = codeMapper.getNumOfTotalCodeByUserId(pageUtil);
        for (Code code : codes) {
            Integer teamId = code.getTeamId();
            if (teamId == null) {
                code.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                code.setTeamName(teamName);
            }
        }
        PageResult pageResult = new PageResult(codes, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public Boolean add_file(Code code){
        return codeMapper.insertSelective(code) > 0;
    }
    //更新文献信息
    public Boolean updateCodeInfo(Code code){
        int radd = codeMapper.updateByPrimaryKeySelective(code);
        if (radd > 0){return true;}
        else {return false;}
    }
    public PageResult getCodePageByUserIdOrderByTeamId(PageQueryUtil pageUtil){
        List<Code> codes = codeMapper.findAllCodeListByUserIdOrderByTeamId(pageUtil);
        int total = codeMapper.getNumOfTotalCodeByUserId(pageUtil);
        for (Code code : codes) {
            Integer teamId = code.getTeamId();
            if (teamId == null) {
                code.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                code.setTeamName(teamName); // 假设 Datasets 类有 setTeamName 方法用于设置团队名称
            }
        }
        PageResult pageResult = new PageResult(codes, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public Code getCodeByCodeId(Integer CodeId){
        return codeMapper.selectByCodeId(CodeId);
    }
    public List<Code> getCodeByTeamId(PageQueryUtil pageUtil, Integer[] teamIdsArray){
        return codeMapper.selectByTeamIds(pageUtil,teamIdsArray);
    }

    public PageResult getCodePage(PageQueryUtil pageUtil){
        List<Userteam> userteams = userteamService.getTeamByPageUtil(pageUtil);
        List<Integer> teamIds = new ArrayList<>();

        for (Userteam userteam : userteams) {
            Integer teamId = userteam.getTeamId();
            teamIds.add(teamId); // 将 teamId 存入 teamIds 列表
        }
        Integer[] teamIdsArray = teamIds.toArray(new Integer[0]);
        List<Code> codes = getCodeByTeamId(pageUtil,teamIdsArray);
        int total = codeMapper.getNumOfUserCode(teamIdsArray);
        for (Code code : codes) {
            Integer teamId = code.getTeamId();
            if (teamId == null) {
                code.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                code.setTeamName(teamName);
            }
        }
        PageResult pageResult = new PageResult(codes, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return codeMapper.deleteBatch(ids) > 0;
    }

}
