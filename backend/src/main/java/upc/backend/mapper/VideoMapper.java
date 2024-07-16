package upc.backend.mapper;
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
    int updateByPrimaryKey(Video video);
    //   Boolean updateByPrimaryKeySelective(video video);


    Video getVideoByID(Integer VideoId);
    List<Video> findAllVideoList(PageQueryUtil pageUtil);
    int getNumOfTotalVideos(PageQueryUtil pageUtil);

}
