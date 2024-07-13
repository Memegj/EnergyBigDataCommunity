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
