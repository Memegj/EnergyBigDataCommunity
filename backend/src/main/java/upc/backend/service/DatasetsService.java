package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Datasets;
import upc.backend.entity.UploadFile;
import upc.backend.mapper.DatasetsMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;

@Service
public class DatasetsService {
    @Resource
    private DatasetsMapper datasetsMapper;
    @Resource
    private TeamService teamService;


    // 获取文献信息
    public Datasets getDatasetsByDataId(Integer DataId){
        return datasetsMapper.selectByDataId(DataId);
    }

    //更新文献信息
    public Boolean updateDatasetsInfo(Datasets datasets){
        int radd = datasetsMapper.updateByPrimaryKeySelective(datasets);
        if (radd > 0){return true;}
        else {return false;}
    }

    public PageResult getDatasetsPage(PageQueryUtil pageUtil){
        List<Datasets> datasets = datasetsMapper.findAllDatasetsList(pageUtil);
        int total = datasetsMapper.getNumOfTotalDatasets(pageUtil);
        PageResult pageResult = new PageResult(datasets, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public PageResult getDatasetsPageByUserId(PageQueryUtil pageUtil){
        List<Datasets> datasets = datasetsMapper.findAllDatasetsListByUserId(pageUtil);
        int total = datasetsMapper.getNumOfTotalDatasetsByUserId(pageUtil);
        for (Datasets dataset : datasets) {
            Integer teamId = dataset.getTeamId();
            if (teamId == null) {
                dataset.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                dataset.setTeamName(teamName); // 假设 Datasets 类有 setTeamName 方法用于设置团队名称
            }
        }
        PageResult pageResult = new PageResult(datasets, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public PageResult getDatasetsPageByUserIdOrderByTime(PageQueryUtil pageUtil){
        List<Datasets> datasets = datasetsMapper.findAllDatasetsListByUserIdOrderByTime(pageUtil);
        int total = datasetsMapper.getNumOfTotalDatasetsByUserId(pageUtil);
        for (Datasets dataset : datasets) {
            Integer teamId = dataset.getTeamId();
            if (teamId == null) {
                dataset.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                dataset.setTeamName(teamName);
            }
        }
        PageResult pageResult = new PageResult(datasets, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public PageResult getDatasetsPageByUserIdOrderByTeamId(PageQueryUtil pageUtil){
        List<Datasets> datasets = datasetsMapper.findAllDatasetsListByUserIdOrderByTeamId(pageUtil);
        int total = datasetsMapper.getNumOfTotalDatasetsByUserId(pageUtil);
        for (Datasets dataset : datasets) {
            Integer teamId = dataset.getTeamId();
            if (teamId == null) {
                dataset.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                dataset.setTeamName(teamName); // 假设 Datasets 类有 setTeamName 方法用于设置团队名称
            }
        }
        PageResult pageResult = new PageResult(datasets, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public Boolean add_datasets(Datasets datasets){
        int radd = datasetsMapper.insertSelective(datasets);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean add_file(Datasets datasets){
        return datasetsMapper.insertSelective(datasets) > 0;
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return datasetsMapper.deleteBatch(ids) > 0;
    }
}
