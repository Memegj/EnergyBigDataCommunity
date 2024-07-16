package upc.backend.mapper;
import org.apache.ibatis.annotations.Mapper;
import upc.backend.entity.Collect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CollectMapper {


    @Mapper
    Collect findByUserIdAndVideoId(@Param("userId") Integer userId, @Param("videoId") Integer videoId);
    int insert(Collect collect);
    int deleteByUserIdAndVideoId(@Param("userId") Integer userId, @Param("videoId") Integer videoId);

    void deleteById(int id);

}
