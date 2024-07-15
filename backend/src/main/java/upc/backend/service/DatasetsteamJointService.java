package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Datasets;
import upc.backend.mapper.DatasetsteamJointMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;

@Service
public class DatasetsteamJointService {
    @Resource
    private DatasetsteamJointMapper datasetsteamJointMapper;


    public PageResult getDatasetsPage(PageQueryUtil pageUtil){
        List<Datasets> datasets = datasetsteamJointMapper.findAllDatasetsList(pageUtil);
        int total = datasetsteamJointMapper.getNumOfTotalDatasets(pageUtil);
        PageResult pageResult = new PageResult(datasets, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public PageResult getDatasets_teamPageByUserId(PageQueryUtil pageUtil){
        List<Datasets> datasets = datasetsteamJointMapper.findAllDatasets_teamListByUserId(pageUtil);
        int total = datasetsteamJointMapper.getNumOfTotalDatasets_teamByUserId(pageUtil);
        PageResult pageResult = new PageResult(datasets, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public Boolean add_datasets(Datasets datasets){
        int radd = datasetsteamJointMapper.insertSelective(datasets);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean add_file(Datasets datasets){
        return datasetsteamJointMapper.insertSelective(datasets) > 0;
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return datasetsteamJointMapper.deleteBatch(ids) > 0;
    }
}
