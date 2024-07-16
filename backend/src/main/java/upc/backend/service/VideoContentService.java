package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import upc.backend.entity.VideoContent;
import upc.backend.mapper.VideoContentMapper;

import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoContentService {
    @Resource
    private VideoContentMapper videoContentMapper;



    public PageResult getVideoContentPage(PageQueryUtil pageUtil) {
        List<VideoContent> videoContent = videoContentMapper.findAllVideocontentList(pageUtil);
        int total = videoContentMapper.getNumOfTotalVideocontent(pageUtil);
        return new PageResult(videoContent, total, pageUtil.getLimit(), pageUtil.getPage());
    }

}
