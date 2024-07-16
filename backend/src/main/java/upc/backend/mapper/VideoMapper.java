package upc.backend.mapper;
import upc.backend.entity.Reference;
import upc.backend.util.PageQueryUtil;
import upc.backend.entity.video;
import java.util.List;

public interface VideoMapper {
//    int insert(Reference reference);
//
//    int insertSelective(Reference reference);
//
//    int deleteBatch(Integer[] ids);
    List<video> selectByVideoName(String title);
    List<video> selectByVideoTeacher(String author);
    int updateByPrimaryKeySelective(video video);
    int updateByPrimaryKey(video video);
 //   Boolean updateByPrimaryKeySelective(video video);


    video getVideoByID(Integer VideoId);
    List<video> findAllVideoList(PageQueryUtil pageUtil);
    int getNumOfTotalVideos(PageQueryUtil pageUtil);

    int deleteBatchVideo(Integer[] ids);
    int deleteBatchCollect(Integer[] ids);

}
