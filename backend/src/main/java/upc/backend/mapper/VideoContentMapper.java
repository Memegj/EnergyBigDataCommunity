package upc.backend.mapper;

import upc.backend.entity.VideoContent;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface VideoContentMapper {

    List<VideoContent> findAllVideocontentList(PageQueryUtil pageUtil);
    int getNumOfTotalVideocontent(PageQueryUtil pageUtil);
}
