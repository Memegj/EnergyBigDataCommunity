package upc.backend.mapper;
import org.apache.ibatis.annotations.Param;
import upc.backend.util.PageQueryUtil;
import upc.backend.entity.Video;
import java.util.List;

public interface VideoMapper {
//    int insert(Reference reference);
//
//    int insertSelective(Reference reference);
//
//    int deleteBatch(Integer[] ids);
    List<Video> selectByVideoName(String title);
    List<Video> selectByVideoTeacher(String author);
    int updateByPrimaryKeySelective(Video video);
    int updateByPrimaryKey(Video video);
 //   Boolean updateByPrimaryKeySelective(video video);


    Video getVideoByID(Integer VideoId);
    List<Video> findAllVideoList(PageQueryUtil pageUtil);
    int getNumOfTotalVideos(PageQueryUtil pageUtil);

    int deleteBatchVideo(Integer[] ids);
    int deleteBatchCollect(Integer[] ids);

    List<Video> selectVideoByTeamIds(@Param("pageUtil") PageQueryUtil pageUtil, @Param("teamIdsArray") Integer[] teamIdsArray);

    int getNumOfUserVideo(PageQueryUtil pageUtil,Integer[] teamIdsArray);

}
