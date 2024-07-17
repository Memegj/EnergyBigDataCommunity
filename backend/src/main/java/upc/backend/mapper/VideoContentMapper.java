package upc.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import upc.backend.entity.Video;
import upc.backend.entity.Videocontent;
import upc.backend.util.PageQueryUtil;

import java.util.List;
@Mapper
public interface VideoContentMapper {
    int deleteBatchVideocontent(Integer[] ids);
    List<Videocontent> selectByVideoId(@Param("videoId") Integer videoId);
}
