package upc.backend.mapper;
import org.apache.ibatis.annotations.Param;
import upc.backend.entity.Datasets;
import upc.backend.entity.Reference;
import upc.backend.entity.Video;
import upc.backend.util.PageQueryUtil;
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

    Video selectByVideoId(Integer VideoId);
    int getNumOfTotalVideos(PageQueryUtil pageUtil);

    int insert(Video video);

    int insertSelective(Video video);

    List<Video> selectByisPublic(Integer isPublic);

    Video selectByVideoIdAndUserId(Integer VideoId);

    int updateByPrimaryKey(Video video);
    List<Video> selectByTeamIds(@Param("pageUtil") PageQueryUtil pageUtil, @Param("teamIdsArray") Integer[] teamIdsArray);
    int getNumOfUserVideo(Integer[] teamIdsArray);
    List<Video> findAllVideoList(PageQueryUtil pageUtil);
    List<Video> findAllVideoListByUserId(PageQueryUtil pageUtil);
    List<Video> findAllVideoListByUserIdOrderByTime(PageQueryUtil pageUtil);
    List<Video> findAllVideoListByUserIdOrderByTeamId(PageQueryUtil pageUtil);
    int getNumOfTotalVideo(PageQueryUtil pageUtil);
    int getNumOfTotalVideoByUserId(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids);
}
