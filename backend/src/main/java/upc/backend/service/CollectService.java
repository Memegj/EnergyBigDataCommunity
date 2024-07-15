package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Collect;
import upc.backend.entity.Reference;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import upc.backend.mapper.CollectMapper;
import upc.backend.util.CollectQueryUtil;

import java.util.List;

@Service
public class CollectService {
    @Resource
    private  CollectMapper collectMapper;


    public PageResult getCollectionByUserId(PageQueryUtil pageUtil){
        List<Collect> collect = collectMapper.getCollectionByUserId(pageUtil);
        int total = collectMapper.getNumOfTotalCollect(pageUtil);
        PageResult pageResult = new PageResult(collect, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    // 获取文献信息
    public Collect getCollectByDataId(CollectQueryUtil collectUtil){

        return collectMapper.selectByDataId(collectUtil);
    }
    public Collect getCollectByCodeId(CollectQueryUtil collectUtil){

        return collectMapper.selectByCodeId(collectUtil);
    }
    public Collect getCollectByLiterId(CollectQueryUtil collectUtil){

        return collectMapper.selectByLiterId(collectUtil);
    }
    public Collect getCollectByVideoId(CollectQueryUtil collectUtil){

        return collectMapper.selectByVideoId(collectUtil);
    }

    public List<Collect> getCollectByCollectType(String CollectType){

        return collectMapper.selectByCollectType(CollectType);
    }

    //更新文献信息
    public Boolean updateCollectInfo(Collect collect){
        int radd = collectMapper.updateByPrimaryKeySelective(collect);
        if (radd > 0){return true;}
        else {return false;}
    }

    public PageResult getCollectPage(PageQueryUtil pageUtil){
        List<Collect> collect = collectMapper.findAllCollectList(pageUtil);
        int total = collectMapper.getNumOfTotalCollect(pageUtil);
        PageResult pageResult = new PageResult(collect, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public boolean deleteBatch(Long[] collectIds, Integer userId) {
        for (Long collectId : collectIds) {

            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("collectId", collectId);
            collectMapper.deleteCollectById(params);

        }
        return true;
    }

    public Boolean add_collect(Collect collect){
        int radd = collectMapper.insertSelective(collect);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean deleteBatchByDataId(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return collectMapper.deleteBatchByDataId(ids) > 0;
    }
    public Boolean deleteBatchByCodeId(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return collectMapper.deleteBatchByCodeId(ids) > 0;
    }
    public Boolean deleteBatchByLiterId(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return collectMapper.deleteBatchByLiterId(ids) > 0;
    }
    public Boolean deleteBatchByVideoId(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return collectMapper.deleteBatchByVideoId(ids) > 0;
    }
    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return collectMapper.deleteBatch(ids) > 0;
    }

}
