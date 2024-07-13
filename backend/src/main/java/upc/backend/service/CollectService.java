package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Collect;
import upc.backend.entity.Reference;
import upc.backend.mapper.CollectMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public PageResult getCollectionByUserId1(PageQueryUtil pageUtil){
        List<Collect> collect = collectMapper.getCollectionByUserId1(pageUtil);
        int total = collectMapper.getNumOfTotalCollect1(pageUtil);
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
}