package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Reference;
import upc.backend.entity.Video;
import upc.backend.mapper.ReferenceMapper;
import upc.backend.mapper.VideoMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;


@Service
public class videoService {
    @Resource
    private VideoMapper videoMapper;
    //获取视频信息
    public Video getVideoById(Integer VideoId){
        return videoMapper.getVideoByID(VideoId);
    }
   //更新视频信息
    public Boolean updateVideoInfo(Video video){
        return videoMapper.updateByPrimaryKeySelective(video) > 0;
    }
    //页数
    public PageResult getVideosPage(PageQueryUtil pageUtil){
        List<Video> videos =videoMapper.findAllVideoList(pageUtil);
        int total = videoMapper.getNumOfTotalVideos(pageUtil);
        PageResult pageResult = new PageResult(videos, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
