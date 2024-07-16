package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.backend.entity.Collect;
import upc.backend.mapper.CollectMapper;
import upc.backend.util.CollectQueryUtil;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;

@Service
public class CollectService {
    @Resource
    private CollectMapper collectMapper;



    public Collect getCollectByUserIdAndVideoId(Integer userId, Integer videoId) {
        return collectMapper.findByUserIdAndVideoId(userId, videoId);
    }


    public boolean addCollect(Collect collect) {
        return collectMapper.insert(collect) > 0;
    }
    public void deleteById(int id) {
        collectMapper.deleteById(id);
    }
    public boolean removeCollectByUserIdAndVideoId(Integer userId, Integer videoId) {
        return collectMapper.deleteByUserIdAndVideoId(userId, videoId) > 0;
    }
}
