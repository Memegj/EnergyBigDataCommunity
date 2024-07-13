package upc.backend.mapper;

import org.apache.ibatis.annotations.Param;
import upc.backend.entity.Collect;
import upc.backend.entity.Team;
import upc.backend.util.PageQueryUtil;

import java.util.List;
import java.util.Map;

public interface CollectMapper {
 //   List<Collect> getCollectionByUserId(@Param("userId") PageQueryUtil userId);
    List<Collect> getCollectionByUserId(PageQueryUtil pageUtil);
    int getNumOfTotalCollect(PageQueryUtil pageUtil);
    List<Collect> getCollectionByUserId1(PageQueryUtil pageUtil);
    int getNumOfTotalCollect1(PageQueryUtil pageUtil);
    void deleteCollectById(Map<String, Object> params);
}
